package com.anbang.qipai.members.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.cqrs.c.domain.CreateMemberResult;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.cqrs.q.dbo.AuthorizationDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldRecordDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreRecordDbo;
import com.anbang.qipai.members.cqrs.q.service.HongBaoQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberGoldQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberScoreQueryService;
import com.anbang.qipai.members.cqrs.q.service.PhoneFeeQueryService;
import com.anbang.qipai.members.msg.service.AuthorizationMsgService;
import com.anbang.qipai.members.msg.service.GoldsMsgService;
import com.anbang.qipai.members.msg.service.MembersMsgService;
import com.anbang.qipai.members.msg.service.ScoresMsgService;
import com.anbang.qipai.members.plan.bean.MemberRightsConfiguration;
import com.anbang.qipai.members.plan.service.MemberRightsConfigurationService;
import com.anbang.qipai.members.util.IPUtil;
import com.anbang.qipai.members.web.vo.CommonVO;
import com.dml.users.AuthorizationAlreadyExistsException;
import com.google.gson.Gson;

@RestController
@RequestMapping("/thirdauth")
public class MemberThirdAuthController {

	private static String APPID = "wxb841e562b0100c95";

	private static String APPSECRET = "c411423c15fdd51bde4ec432732d26df";

	private Gson gson = new Gson();

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

	/**
	 * 客户端已经获取好了openid/unionid和微信用户信息
	 *
	 * @param unionid
	 * @param openid
	 * @param nickname
	 * @param headimgurl
	 * @param sex
	 *            值为1时是男性，值为2时是女性，值为0时是未知
	 * @return
	 */

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
					try {
						memberAuthCmdService.addThirdAuth("open.weixin.app.qipai", openid,
								unionidAuthDbo.getMemberId());
						AuthorizationDbo authDbo = memberAuthQueryService.addThirdAuth("open.weixin.app.qipai", openid,
								unionidAuthDbo.getMemberId());
						authorizationMsgService.newAuthorization(authDbo);
					} catch (AuthorizationAlreadyExistsException e) {
						AuthorizationDbo authDbo = memberAuthQueryService.addThirdAuth("open.weixin.app.qipai", openid,
								unionidAuthDbo.getMemberId());
						authorizationMsgService.newAuthorization(authDbo);
					}
				}
				// 更新用户信息
				memberAuthQueryService.updateMember(unionidAuthDbo.getMemberId(), nickname, headimgurl, sex,
						IPUtil.getRealIp(request));
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
						createMemberResult.getMemberId(), "union.weixin", unionid, memberRightsConfiguration, false,
						IPUtil.getRealIp(request));
				authorizationMsgService.newAuthorization(unionAuthDbo);
				// 添加openid授权
				memberAuthCmdService.addThirdAuth("open.weixin.app.qipai", openid, unionAuthDbo.getMemberId());
				AuthorizationDbo openAuthDbo = memberAuthQueryService.addThirdAuth("open.weixin.app.qipai", openid,
						unionAuthDbo.getMemberId());
				authorizationMsgService.newAuthorization(openAuthDbo);
				// 填充用户信息
				memberAuthQueryService.updateMember(createMemberResult.getMemberId(), nickname, headimgurl, sex,
						IPUtil.getRealIp(request));
				// 发送消息
				MemberDbo memberDbo = memberAuthQueryService.findMemberById(createMemberResult.getMemberId());
				membersMsgService.createMember(memberDbo);

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

	/**
	 * 公众号注册
	 */
	@RequestMapping(value = "/wechatidlogin_gongzhonghao")
	@ResponseBody
	public CommonVO wechatidlogin_gongzhonghao(String unionid, String openid, String nickname, String headimgurl,
			Integer sex, String reqIP) {
		CommonVO vo = new CommonVO();
		try {
			AuthorizationDbo unionidAuthDbo = memberAuthQueryService.findThirdAuthorizationDbo("union.weixin", unionid);
			if (unionidAuthDbo != null) {// 已unionid注册
				AuthorizationDbo openidAuthDbo = memberAuthQueryService
						.findThirdAuthorizationDbo("open.weixin.gongzhonghao.qipai", openid);
				if (openidAuthDbo == null) {// openid未注册
					// 添加openid授权
					memberAuthCmdService.addThirdAuth("open.weixin.gongzhonghao.qipai", openid,
							unionidAuthDbo.getMemberId());
					AuthorizationDbo authDbo = memberAuthQueryService.addThirdAuth("open.weixin.gongzhonghao.qipai",
							openid, unionidAuthDbo.getMemberId());
					authorizationMsgService.newAuthorization(authDbo);
				}
				// 更新用户信息
				memberAuthQueryService.updateMember(unionidAuthDbo.getMemberId(), nickname, headimgurl, sex, reqIP);
				// 发送消息
				MemberDbo memberDbo = memberAuthQueryService.findMemberById(unionidAuthDbo.getMemberId());
				membersMsgService.updateMemberBaseInfo(memberDbo);

				// openid登录
				String token = memberAuthService.thirdAuth("open.weixin.gongzhonghao.qipai", openid);

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
						createMemberResult.getMemberId(), "union.weixin", unionid, memberRightsConfiguration, false,
						reqIP);
				authorizationMsgService.newAuthorization(unionAuthDbo);
				// 添加openid授权
				memberAuthCmdService.addThirdAuth("open.weixin.gongzhonghao.qipai", openid, unionAuthDbo.getMemberId());
				AuthorizationDbo openAuthDbo = memberAuthQueryService.addThirdAuth("open.weixin.gongzhonghao.qipai",
						openid, unionAuthDbo.getMemberId());
				authorizationMsgService.newAuthorization(openAuthDbo);
				// 填充用户信息
				memberAuthQueryService.updateMember(createMemberResult.getMemberId(), nickname, headimgurl, sex, reqIP);
				// 发送消息
				MemberDbo memberDbo = memberAuthQueryService.findMemberById(createMemberResult.getMemberId());
				membersMsgService.createMember(memberDbo);

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

	/**
	 * 获取网页授权access_token
	 *
	 * @param code
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	private Map takeOauth2AccessToken(String code) throws InterruptedException, ExecutionException, TimeoutException {
		Map data = new HashMap();
		try {
			// 拼接请求地址
			String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";// ?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
			Request request = sslHttpClient.POST(requestUrl).timeout(2, TimeUnit.SECONDS);
			request.param("appid", APPID);
			request.param("secret", APPSECRET);
			request.param("code", code);
			request.param("grant_type", "authorization_code");
			ContentResponse response = request.send();
			String content = response.getContentAsString();
			return gson.fromJson(content, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
			return data;
		}
	}

	private Map takeUserInfo(String accessToken, String openid)
			throws InterruptedException, ExecutionException, TimeoutException {
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openid
				+ "&lang=zh_CN";
		String content = sslHttpClient.POST(url).timeout(2, TimeUnit.SECONDS).send().getContentAsString();
		return gson.fromJson(content, Map.class);
	}
}
