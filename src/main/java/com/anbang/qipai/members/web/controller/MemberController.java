package com.anbang.qipai.members.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.config.RealNameVerifyConfig;
import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.cqrs.c.service.MemberGoldCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberScoreCmdService;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldAccountDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldRecordDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreAccountDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreRecordDbo;
import com.anbang.qipai.members.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberGoldQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberScoreQueryService;
import com.anbang.qipai.members.msg.service.GoldsMsgService;
import com.anbang.qipai.members.msg.service.MembersMsgService;
import com.anbang.qipai.members.msg.service.ScoresMsgService;
import com.anbang.qipai.members.plan.service.MemberService;
import com.anbang.qipai.members.remote.service.QiPaiAgentsRemoteService;
import com.anbang.qipai.members.remote.vo.CommonRemoteVO;
import com.anbang.qipai.members.util.HttpUtil;
import com.anbang.qipai.members.web.vo.CommonVO;
import com.anbang.qipai.members.web.vo.DetailsVo;
import com.anbang.qipai.members.web.vo.MemberVO;
import com.dml.accounting.AccountingRecord;
import com.google.gson.Gson;
import com.highto.framework.web.page.ListPage;

/**
 * 会员controller
 * 
 * @author 林少聪 2018.7.9
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MemberAuthService memberAuthService;
	@Autowired
	private MemberAuthQueryService memberAuthQueryService;

	@Autowired
	private MemberGoldQueryService memberGoldQueryService;

	@Autowired
	private MemberScoreQueryService memberScoreQueryService;

	@Autowired
	private MemberGoldCmdService memberGoldCmdService;

	@Autowired
	private MemberScoreCmdService memberScoreCmdService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private MembersMsgService membersMsgService;

	@Autowired
	private GoldsMsgService goldsMsgService;

	@Autowired
	private ScoresMsgService scoresMsgService;

	@Autowired
	private QiPaiAgentsRemoteService qiPaiAgentsRemoteService;

	private Gson gson = new Gson();

	@RequestMapping(value = "/info")
	public MemberVO info(String memberId) {
		MemberVO vo = new MemberVO();
		MemberDbo memberDbo = memberAuthQueryService.findMember(memberId);
		if (memberDbo == null) {
			vo.setSuccess(false);
			return vo;
		}
		vo.setSuccess(true);
		vo.setHeadimgurl(memberDbo.getHeadimgurl());
		vo.setMemberId(memberId);
		vo.setNickname(memberDbo.getNickname());
		vo.setVerifyUser(memberDbo.isVerifyUser());
		MemberGoldAccountDbo memberGoldAccountDbo = memberGoldQueryService.findMemberGoldAccount(memberId);
		if (memberGoldAccountDbo != null) {
			vo.setGold(memberGoldAccountDbo.getBalance());
		}
		MemberScoreAccountDbo memberScoreAccountDbo = memberScoreQueryService.findMemberScoreAccount(memberId);
		if (memberScoreAccountDbo != null) {
			vo.setScore(memberScoreAccountDbo.getBalance());
		}
		return vo;
	}

	@RequestMapping("/querymember")
	public DetailsVo queryMember(String token) {
		DetailsVo vo = new DetailsVo();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		MemberGoldAccountDbo memberGoldAccountDbo = memberGoldQueryService.findMemberGoldAccount(memberId);
		if (memberGoldAccountDbo != null) {
			vo.setGold(memberGoldAccountDbo.getBalance());
		}
		MemberScoreAccountDbo memberScoreAccountDbo = memberScoreQueryService.findMemberScoreAccount(memberId);
		if (memberScoreAccountDbo != null) {
			vo.setScore(memberScoreAccountDbo.getBalance());
		}
		MemberDbo member = memberService.findMemberById(memberId);
		vo.setVipLevel(member.getVipLevel());
		vo.setPhone(member.getPhone());
		String vipEndTime = "";
		if (member.getVipEndTime() != null && member.getVipEndTime() > System.currentTimeMillis()) {
			long endTime = member.getVipEndTime();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			vipEndTime = format.format(new Date(endTime));
		}
		vo.setVipEndTime(vipEndTime);
		vo.setSuccess(true);
		vo.setMsg("information");
		return vo;
	}

	/**
	 * 实名认证
	 */
	@RequestMapping("/verify")
	public CommonVO verifyMember(String realName, String IDcard, String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String birthString = IDcard.substring(6, 14);
		String year = birthString.substring(0, 4);
		int targetAge = Integer.parseInt(year) + 18;
		String targetBirth = targetAge + IDcard.substring(10, 14);
		try {
			Date targetDate = format.parse(targetBirth);
			if (System.currentTimeMillis() - targetDate.getTime() < 0) {
				vo.setSuccess(false);
				vo.setMsg("too young");
				return vo;
			}
			String host = "https://idcert.market.alicloudapi.com";
			String path = "/idcard";
			String method = "GET";
			String appcode = RealNameVerifyConfig.APPCODE;
			Map<String, String> headers = new HashMap<String, String>();
			// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
			headers.put("Authorization", "APPCODE " + appcode);
			Map<String, String> querys = new HashMap<String, String>();
			querys.put("idCard", IDcard);
			querys.put("name", realName);

			HttpResponse response = HttpUtil.doGet(host, path, method, headers, querys);
			Map map = gson.fromJson(EntityUtils.toString(response.getEntity()), Map.class);
			String status = (String) map.get("status");
			// 认证成功
			if ("01".equals(status)) {
				String name = (String) map.get("name");
				String idCard = (String) map.get("idCard");
				MemberDbo member = new MemberDbo();
				member.setId(memberId);
				member.setRealName(name);
				member.setIDcard(idCard);
				member.setVerifyUser(true);
				membersMsgService.verifyMember(member);
				return vo;
			}
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		vo.setSuccess(false);
		return vo;
	}

	@RequestMapping("/registerphone")
	public CommonVO registerPhone(String phone, String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		memberService.registerPhone(memberId, phone);
		MemberDbo memberdbo = memberService.findMemberById(memberId);
		// kafka发消息更新
		membersMsgService.updateMemberPhone(memberdbo);
		vo.setSuccess(true);
		vo.setMsg("register success");
		vo.setData(phone);
		return vo;
	}

	@RequestMapping("/agentinvite")
	public CommonVO agentInvite(String token, String invitationCode) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		MemberDbo member = memberService.findMemberById(memberId);
		CommonRemoteVO commonRemoteVo = qiPaiAgentsRemoteService.agent_invitemember(member.getId(),
				member.getNickname(), invitationCode);
		vo.setSuccess(commonRemoteVo.isSuccess());
		vo.setMsg(commonRemoteVo.getMsg());
		return vo;
	}

	@RequestMapping("/querygoldaccount")
	public CommonVO queryGoldAccount(@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "size", defaultValue = "10") Integer size, String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId != null) {
			ListPage listPage = memberGoldQueryService.findMemberGoldRecords(page, size, memberId);
			vo.setSuccess(true);
			vo.setMsg("goldaccout");
			vo.setData(listPage);
		} else {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
		}
		return vo;
	}

	@RequestMapping("/queryscoreaccount")
	public CommonVO queryScoreAccount(@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "size", defaultValue = "10") Integer size, String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId != null) {
			ListPage listPage = memberScoreQueryService.findMemberScoreRecords(page, size, memberId);
			vo.setSuccess(true);
			vo.setMsg("scoreaccout");
			vo.setData(listPage);
		} else {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
		}
		return vo;
	}

	/**
	 * 管理后台赠送积分或金币，修改积分金币
	 * 
	 * @throws MemberNotFoundException
	 **/
	@RequestMapping("/update_score_gold")
	public CommonVO update_score_gold(@RequestBody String[] ids, Integer score, Integer gold) {
		CommonVO vo = new CommonVO();
		vo.setSuccess(true);
		for (String id : ids) {
			try {
				if (score != null) {
					// 添加积分
					AccountingRecord scorercd = memberScoreCmdService.giveScoreToMember(id, score, "admin_give_score",
							System.currentTimeMillis());
					MemberScoreRecordDbo scoredbo = memberScoreQueryService.withdraw(id, scorercd);
					scoresMsgService.withdraw(scoredbo);
				}
				if (gold != null) {
					// 添加金币
					AccountingRecord goldrcd = memberGoldCmdService.giveGoldToMember(id, gold, "admin_give_gold",
							System.currentTimeMillis());
					MemberGoldRecordDbo golddbo = memberGoldQueryService.withdraw(id, goldrcd);
					goldsMsgService.withdraw(golddbo);
				}
			} catch (MemberNotFoundException e) {
				vo.setSuccess(false);
				vo.setMsg("member not found");
				e.printStackTrace();
			}
		}
		return vo;
	}

	@RequestMapping("/rechargevip")
	public CommonVO rechargeVip(String memberId, Long vipEndTime) {
		CommonVO vo = new CommonVO();
		memberService.updateMemberVip(memberId, vipEndTime);
		MemberDbo member = memberService.findMemberById(memberId);
		membersMsgService.updateMemberVip(member);
		vo.setSuccess(true);
		vo.setMsg("success");
		return vo;
	}

	/**
	 * 会员到期判定
	 * 
	 */
	@Scheduled(cron = "0 0 0/8 * * ?") // 每8小时更新一次
	public void resetVIP() {
		int size = 2000;
		long amount = memberService.getAmount();
		long pageCount = amount / size > 0 ? amount / size + 1 : amount / size;
		for (int page = 1; page <= pageCount; page++) {
			List<MemberDbo> memberList = memberService.findMemberByVip(page, size, true);
			for (MemberDbo member : memberList) {
				if (member.getVipEndTime() < System.currentTimeMillis()) {
					member.setVip(false);
					memberService.resetVIP(member);
					// kafka更新
					membersMsgService.resetMemberVip(member);
				}
			}
		}
	}
}
