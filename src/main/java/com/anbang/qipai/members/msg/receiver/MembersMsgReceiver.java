package com.anbang.qipai.members.msg.receiver;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.members.cqrs.c.domain.CreateMemberResult;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberGoldCmdService;
import com.anbang.qipai.members.cqrs.q.dbo.AuthorizationDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldRecordDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberRights;
import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreRecordDbo;
import com.anbang.qipai.members.cqrs.q.service.HongBaoQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberGoldQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberScoreQueryService;
import com.anbang.qipai.members.cqrs.q.service.PhoneFeeQueryService;
import com.anbang.qipai.members.msg.channel.sink.MembersSink;
import com.anbang.qipai.members.msg.msjobj.CommonMO;
import com.anbang.qipai.members.msg.service.GoldsMsgService;
import com.anbang.qipai.members.msg.service.MembersMsgService;
import com.anbang.qipai.members.msg.service.ScoresMsgService;
import com.anbang.qipai.members.plan.bean.MemberRightsConfiguration;
import com.anbang.qipai.members.plan.service.MemberRightsConfigurationService;
import com.anbang.qipai.members.remote.service.QiPaiAgentsRemoteService;
import com.anbang.qipai.members.remote.vo.CommonRemoteVO;
import com.dml.accounting.AccountingRecord;
import com.google.gson.Gson;

@EnableBinding(MembersSink.class)
public class MembersMsgReceiver {

	@Autowired
	private MemberAuthCmdService memberAuthCmdService;

	@Autowired
	private MemberAuthQueryService memberAuthQueryService;

	@Autowired
	private HongBaoQueryService hongBaoQueryService;

	@Autowired
	private PhoneFeeQueryService phoneFeeQueryService;

	@Autowired
	private MemberGoldQueryService memberGoldQueryService;

	@Autowired
	private MemberScoreQueryService memberScoreQueryService;

	@Autowired
	private MembersMsgService membersMsgService;

	@Autowired
	private GoldsMsgService goldsMsgService;

	@Autowired
	private MemberGoldCmdService memberGoldCmdService;

	@Autowired
	private ScoresMsgService scoresMsgService;

	@Autowired
	private QiPaiAgentsRemoteService qiPaiAgentsRemoteService;

	@Autowired
	private MemberRightsConfigurationService memberRightsConfigurationService;

	private Gson gson = new Gson();

	@StreamListener(MembersSink.MEMBERSLOGIN)
	public void thirdauth_wechatidlogin(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if ("new member".equals(msg)) {
			Map data = gson.fromJson(json, Map.class);
			String invitationCode = (String) data.get("invitationCode");
			String unionid = (String) data.get("unionid");
			String openid = (String) data.get("openid");
			String nickname = (String) data.get("nickname");
			String headimgurl = (String) data.get("headimgurl");
			int sex = ((Double) data.get("sex")).intValue();
			try {
				AuthorizationDbo unionidAuthDbo = memberAuthQueryService.findThirdAuthorizationDbo("union.weixin",
						unionid);
				MemberDbo memberDbo = null;
				if (unionidAuthDbo != null) {// 已unionid注册
					AuthorizationDbo openidAuthDbo = memberAuthQueryService
							.findThirdAuthorizationDbo("open.weixin.app.qipai", openid);
					if (openidAuthDbo == null) {// openid未注册
						// 添加openid授权
						memberAuthCmdService.addThirdAuth("open.weixin.app.qipai", openid,
								unionidAuthDbo.getMemberId());
						memberAuthQueryService.addThirdAuth("open.weixin.app.qipai", openid,
								unionidAuthDbo.getMemberId());
					}
					memberDbo = memberAuthQueryService.findMemberById(unionidAuthDbo.getMemberId());
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
					CreateMemberResult createMemberResult = memberAuthCmdService.createMemberAndAddThirdAuth(
							"union.weixin", unionid, goldForNewMember, scoreForNewMember, System.currentTimeMillis());

					memberAuthQueryService.createMemberAndAddThirdAuth(createMemberResult.getMemberId(), "union.weixin",
							unionid, memberRightsConfiguration);

					// 填充用户信息
					memberAuthQueryService.updateMember(createMemberResult.getMemberId(), nickname, headimgurl, sex);
					// 发送消息
					memberDbo = memberAuthQueryService.findMemberById(createMemberResult.getMemberId());
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
				}
				CommonRemoteVO commonRemoteVo = qiPaiAgentsRemoteService.agent_invitemember(memberDbo.getId(),
						memberDbo.getNickname(), invitationCode);
				if ("invitation already exist".equals(commonRemoteVo.getMsg())) {
					Map map = (Map) commonRemoteVo.getData();
					memberDbo = memberAuthQueryService.updateMemberBindAgent(memberDbo.getId(),
							(String) map.get("agentId"), true);
					membersMsgService.updateMemberBindAgent(memberDbo);
				}
				// 如果用户绑定过推广员,就不发奖了
				if (!memberDbo.isHasBindAgent() && commonRemoteVo.isSuccess()) {
					MemberRights rights = memberDbo.getRights();
					Map map = (Map) commonRemoteVo.getData();
					memberDbo = memberAuthQueryService.updateMemberBindAgent(memberDbo.getId(),
							(String) map.get("agentId"), true);
					membersMsgService.updateMemberBindAgent(memberDbo);
					AccountingRecord rcd = memberGoldCmdService.giveGoldToMember(memberDbo.getId(),
							rights.getGoldForAgentInvite(), "bind invitioncode", System.currentTimeMillis());
					MemberGoldRecordDbo dbo = memberGoldQueryService.withdraw(memberDbo.getId(), rcd);
					// rcd发kafka
					goldsMsgService.withdraw(dbo);
				}
			} catch (Exception e) {

			}
		}
	}
}
