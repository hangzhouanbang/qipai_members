package com.anbang.qipai.members.web.controller;

import com.anbang.qipai.members.cqrs.c.domain.CreateMemberResult;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.cqrs.q.dbo.*;
import com.anbang.qipai.members.cqrs.q.service.*;
import com.anbang.qipai.members.msg.service.AuthorizationMsgService;
import com.anbang.qipai.members.msg.service.GoldsMsgService;
import com.anbang.qipai.members.msg.service.MembersMsgService;
import com.anbang.qipai.members.msg.service.ScoresMsgService;
import com.anbang.qipai.members.plan.bean.MemberRightsConfiguration;
import com.anbang.qipai.members.plan.service.MemberRightsConfigurationService;
import com.anbang.qipai.members.web.vo.CommonVO;
import org.eclipse.jetty.client.HttpClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 吴硕涵
 * @Date: 2019/1/30 3:26 PM
 * @Version 1.0
 */

@RestController
@RequestMapping("/robot")
public class RobotLoginController {


    @Autowired
    protected HttpClient sslHttpClient;

    @Autowired
    private MemberAuthCmdService memberAuthCmdService;

    @Autowired
    private MemberAuthQueryService memberAuthQueryService;

    @Autowired
    private MemberAuthService memberAuthService;

    @Autowired
    private MemberGoldQueryService memberGoldQueryService;

    @Autowired
    private MemberScoreQueryService memberScoreQueryService;

    @Autowired
    private HongBaoQueryService hongBaoQueryService;

    @Autowired
    private PhoneFeeQueryService phoneFeeQueryService;

    @Autowired
    private MembersMsgService membersMsgService;

    @Autowired
    private GoldsMsgService goldsMsgService;

    @Autowired
    private ScoresMsgService scoresMsgService;

    @Autowired
    private MemberRightsConfigurationService memberRightsConfigurationService;

    @Autowired
    private AuthorizationMsgService authorizationMsgService;


    // if 1fail
    @RequestMapping(value = "/wechatidlogin")
    @ResponseBody
    public CommonVO wechatidlogin(HttpServletRequest request, String unionid, String openid, String nickname,
                                  String headimgurl, Integer sex) {
        CommonVO vo = new CommonVO();
        try {
            AuthorizationDbo unionidAuthDbo = memberAuthQueryService.findThirdAuthorizationDbo("union.weixin", unionid);
            if (unionidAuthDbo != null) {// 已unionid注册
                AuthorizationDbo openidAuthDbo = memberAuthQueryService
                        .findThirdAuthorizationDbo("open.weixin.app.qipai", openid);
                if (openidAuthDbo == null) {// openid未注册
                    // 添加openid授权
                    memberAuthCmdService.addThirdAuth("open.weixin.app.qipai", openid, unionidAuthDbo.getMemberId());
                    AuthorizationDbo authDbo = memberAuthQueryService.addThirdAuth("open.weixin.app.qipai", openid,
                            unionidAuthDbo.getMemberId());
                    authorizationMsgService.newAuthorization(authDbo);
                }
                // 更新用户信息
                memberAuthQueryService.updateMember(unionidAuthDbo.getMemberId(), nickname, headimgurl, sex);
                // 发送消息
                MemberDbo memberDbo = memberAuthQueryService.findMemberById(unionidAuthDbo.getMemberId());
                membersMsgService.updateMemberBaseInfo(memberDbo);

                // openid登录
                String token = memberAuthService.thirdAuth("open.weixin.app.qipai", openid);

                vo.setSuccess(true);
                Map data = new HashMap();
                data.put("token", token);
                vo.setData(data);
                return vo;
            } else {
                int goldForNewMember = 0;
                int scoreForNewMember = 0;
                MemberRightsConfiguration memberRightsConfiguration = memberRightsConfigurationService
                        .findMemberRightsConfiguration();
                if (memberRightsConfiguration != null) {
                    goldForNewMember = memberRightsConfiguration.getGoldForNewNember();
                    scoreForNewMember = memberRightsConfiguration.getScoreForNewNember();
                }
                // 创建会员和unionid授权
                CreateMemberResult createMemberResult = memberAuthCmdService.createMemberAndAddThirdAuth("union.weixin",
                        unionid, goldForNewMember, scoreForNewMember, System.currentTimeMillis());

                AuthorizationDbo unionAuthDbo = memberAuthQueryService.createMemberAndAddThirdAuth(
                        createMemberResult.getMemberId(), "union.weixin", unionid, memberRightsConfiguration);
                authorizationMsgService.newAuthorization(unionAuthDbo);
                // 添加openid授权
                memberAuthCmdService.addThirdAuth("open.weixin.app.qipai", openid, unionAuthDbo.getMemberId());
                AuthorizationDbo openAuthDbo = memberAuthQueryService.addThirdAuth("open.weixin.app.qipai", openid,
                        unionAuthDbo.getMemberId());
                authorizationMsgService.newAuthorization(openAuthDbo);
                // 填充用户信息
                memberAuthQueryService.updateMember(createMemberResult.getMemberId(), nickname, headimgurl, sex);
                // 发送消息
                MemberDbo memberDbo = memberAuthQueryService.findMemberById(createMemberResult.getMemberId());

                MemberAdvice memberAdvice = new MemberAdvice();
                BeanUtils.copyProperties(memberDbo, memberAdvice);
                memberAdvice.setRobot(true);
                membersMsgService.createRobotMember(memberAdvice);

                // 创建金币帐户，赠送金币记账
                MemberGoldRecordDbo goldDbo = memberGoldQueryService.createMember(createMemberResult);
                // 创建积分账户，赠送金币记账
                MemberScoreRecordDbo scoreDbo = memberScoreQueryService.createMember(createMemberResult);

                this.hongBaoQueryService.createAccount(createMemberResult);

                this.phoneFeeQueryService.createAccount(createMemberResult);

                // 发送金币记账消息
                goldsMsgService.withdraw(goldDbo);
                // 发送积分记账消息
                scoresMsgService.withdraw(scoreDbo);

                // TODO 发消息

                // unionid登录
                String token = memberAuthService.thirdAuth("union.weixin", unionid);

                vo.setSuccess(true);
                Map data = new HashMap();
                data.put("token", token);
                vo.setData(data);
                return vo;
            }
        } catch (Exception e) {
            vo.setSuccess(false);
            vo.setMsg(e.getClass().toString());
            return vo;
        }
    }
}
