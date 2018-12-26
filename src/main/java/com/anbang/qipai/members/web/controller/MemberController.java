package com.anbang.qipai.members.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.config.PhoneVerifyConfig;
import com.anbang.qipai.members.config.RealNameVerifyConfig;
import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.cqrs.c.service.MemberGoldCmdService;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldAccountDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldRecordDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberRights;
import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreAccountDbo;
import com.anbang.qipai.members.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberGoldQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberScoreQueryService;
import com.anbang.qipai.members.msg.service.GoldsMsgService;
import com.anbang.qipai.members.msg.service.MembersMsgService;
import com.anbang.qipai.members.plan.bean.MemberLoginLimitRecord;
import com.anbang.qipai.members.plan.bean.MemberVerifyPhone;
import com.anbang.qipai.members.plan.service.MemberLoginLimitRecordService;
import com.anbang.qipai.members.plan.service.MemberVerifyPhoneService;
import com.anbang.qipai.members.remote.service.QiPaiAgentsRemoteService;
import com.anbang.qipai.members.remote.vo.CommonRemoteVO;
import com.anbang.qipai.members.util.HttpUtil;
import com.anbang.qipai.members.util.VerifyPhoneCodeUtil;
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
	private MemberVerifyPhoneService memberVerifyPhoneService;

	@Autowired
	private MemberGoldCmdService memberGoldCmdService;

	@Autowired
	private MembersMsgService membersMsgService;

	@Autowired
	private GoldsMsgService goldsMsgService;

	@Autowired
	private QiPaiAgentsRemoteService qiPaiAgentsRemoteService;

	@Autowired
	private MemberLoginLimitRecordService memberLoginLimitRecordService;

	private Gson gson = new Gson();

	@RequestMapping(value = "/info")
	public MemberVO info(String memberId) {
		MemberVO vo = new MemberVO();
		MemberDbo memberDbo = memberAuthQueryService.findMemberById(memberId);
		if (memberDbo == null) {
			vo.setSuccess(false);
			return vo;
		}
		vo.setSuccess(true);
		vo.setHeadimgurl(memberDbo.getHeadimgurl());
		vo.setMemberId(memberId);
		vo.setNickname(memberDbo.getNickname());
		vo.setVerifyUser(memberDbo.isVerifyUser());
		vo.setBindAgent(memberDbo.isBindAgent());
		MemberGoldAccountDbo memberGoldAccountDbo = memberGoldQueryService.findMemberGoldAccount(memberId);
		if (memberGoldAccountDbo != null) {
			int gold = memberGoldAccountDbo.getBalance();
			if (gold > 999999) {
				vo.setGold(gold / 10000 + "w");
			} else {
				vo.setGold(String.valueOf(gold));
			}
		}
		MemberScoreAccountDbo memberScoreAccountDbo = memberScoreQueryService.findMemberScoreAccount(memberId);
		if (memberScoreAccountDbo != null) {
			int score = memberScoreAccountDbo.getBalance();
			if (score > 999999) {
				vo.setScore(score / 10000 + "w");
			} else {
				vo.setScore(String.valueOf(score));
			}
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
		MemberLoginLimitRecord record = memberLoginLimitRecordService.findByMemberId(memberId, true);
		if (record != null) {// 被封号
			vo.setSuccess(false);
			vo.setMsg("login limited");
			return vo;
		}
		MemberGoldAccountDbo memberGoldAccountDbo = memberGoldQueryService.findMemberGoldByMemberId(memberId);
		if (memberGoldAccountDbo != null) {
			int gold = memberGoldAccountDbo.getBalance();
			if (gold > 999999) {
				vo.setGold(gold / 10000 + "w");
			} else {
				vo.setGold(String.valueOf(gold));
			}
		}
		MemberScoreAccountDbo memberScoreAccountDbo = memberScoreQueryService.findMemberScoreAccount(memberId);
		if (memberScoreAccountDbo != null) {
			int score = memberScoreAccountDbo.getBalance();
			if (score > 999999) {
				vo.setScore(score / 10000 + "w");
			} else {
				vo.setScore(String.valueOf(score));
			}
		}
		MemberDbo member = memberAuthQueryService.findMemberById(memberId);
		vo.setVip(member.isVip());
		vo.setVipLevel(member.getVipLevel());
		vo.setPhone(member.getPhone());
		String vipEndTime = "";
		if (member.getVipEndTime() > System.currentTimeMillis()) {
			long endTime = member.getVipEndTime();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			vipEndTime = format.format(new Date(endTime));
		} else {
			member = memberAuthQueryService.updateMemberVip(member.getId(), false);
			// kafka更新
			membersMsgService.updateMemberVip(member);
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
	public CommonVO verifyMember(@RequestParam(required = true) String realName,
			@RequestParam(required = true) String IDcard, String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("无效的凭证");
			return vo;
		}
		if (IDcard.length() != 18 || !Pattern.matches("[0-9]{14}\\S{4}", IDcard)) {
			vo.setSuccess(false);
			vo.setMsg("无效的身份证号");
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
				vo.setMsg("未到法定年龄");
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
			if (map == null) {
				vo.setSuccess(false);
				vo.setMsg("系统异常");
				return vo;
			}
			String status = (String) map.get("status");
			// 认证成功
			if ("01".equals(status)) {
				String name = (String) map.get("name");
				String idCard = (String) map.get("idCard");
				MemberDbo member = memberAuthQueryService.updateMemberRealUser(memberId, name, idCard, true);
				membersMsgService.updateMemberRealUser(member);
				vo.setMsg("认证通过");
				return vo;
			}
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		vo.setSuccess(false);
		vo.setMsg("认证未通过");
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
		MemberDbo member = memberAuthQueryService.findMemberById(memberId);
		if (member.getPhone() != null && !"".equals(member.getPhone())) {
			vo.setSuccess(false);
			vo.setMsg("already registe phone");
			return vo;
		}
		if (!Pattern.matches("[0-9]{11}", phone)) {
			vo.setSuccess(false);
			vo.setMsg("invalid phone");
			return vo;
		}
		try {
			String param = VerifyPhoneCodeUtil.generateVerifyCode();
			String host = "https://feginesms.market.alicloudapi.com";
			String path = "/codeNotice";
			String method = "GET";
			String appcode = PhoneVerifyConfig.APPCODE;
			Map<String, String> headers = new HashMap<String, String>();
			// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
			headers.put("Authorization", "APPCODE " + appcode);
			Map<String, String> querys = new HashMap<String, String>();
			querys.put("param", param);
			querys.put("phone", phone);
			querys.put("sign", "1");
			querys.put("skin", "1");

			HttpResponse response = HttpUtil.doGet(host, path, method, headers, querys);
			Map map = gson.fromJson(EntityUtils.toString(response.getEntity()), Map.class);
			String message = (String) map.get("Message");
			String code = (String) map.get("Code");
			MemberVerifyPhone memberVerifyPhone = new MemberVerifyPhone();
			memberVerifyPhone.setId(memberId);
			memberVerifyPhone.setPhone(phone);
			memberVerifyPhone.setParam(param);
			memberVerifyPhone.setMessage(message);
			memberVerifyPhone.setCode(code);
			if (message.equals("OK") && code.equals("OK")) {
				String requestId = (String) map.get("RequestId");
				String bizId = (String) map.get("BizId");
				memberVerifyPhone.setRequestId(requestId);
				memberVerifyPhone.setBizId(bizId);
				vo.setSuccess(true);
			} else {
				vo.setSuccess(false);
				vo.setMsg(message);
			}
			memberVerifyPhoneService.save(memberVerifyPhone);
			return vo;
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
	}

	@RequestMapping("/verifyphone")
	public CommonVO verifyPhone(String phone, String param, String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		MemberDbo member = memberAuthQueryService.findMemberById(memberId);
		if (member.getPhone() != null && !"".equals(member.getPhone())) {
			vo.setSuccess(true);
			return vo;
		}
		MemberVerifyPhone memberVerifyPhone = memberVerifyPhoneService.findById(memberId);
		if (memberVerifyPhone == null || !memberVerifyPhone.getParam().equals(param)) {
			vo.setSuccess(false);
			vo.setMsg("invalid param");
			return vo;
		}
		if (!phone.equals(memberVerifyPhone.getPhone())) {
			vo.setSuccess(false);
			vo.setMsg("invalid phone");
			return vo;
		}
		member = memberAuthQueryService.registerPhone(memberId, phone);
		membersMsgService.updateMemberPhone(member);
		member = memberAuthQueryService.rechargeVip(memberId, 24L * 60 * 60 * 1000);
		membersMsgService.rechargeVip(member);
		Map data = new HashMap<>();
		data.put("phone", phone);
		String vipEndTime = "";
		if (member.isVip() && member.getVipEndTime() > System.currentTimeMillis()) {
			long endTime = member.getVipEndTime();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			vipEndTime = format.format(new Date(endTime));
		}
		data.put("vipEndTime", vipEndTime);
		vo.setData(data);
		vo.setSuccess(true);
		vo.setMsg("register success");
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
		MemberDbo member = memberAuthQueryService.findMemberById(memberId);
		CommonRemoteVO commonRemoteVo = qiPaiAgentsRemoteService.agent_invitemember(member.getId(),
				member.getNickname(), invitationCode);
		// 如果用户绑定过推广员,就不发奖了
		if (!member.isHasBindAgent() && commonRemoteVo.isSuccess()) {
			MemberRights rights = member.getRights();
			Map data = new HashMap<>();
			data.put("goldForAgentInvite", rights.getGoldForAgentInvite());
			vo.setData(data);
			Map map = (Map) commonRemoteVo.getData();
			member = memberAuthQueryService.updateMemberBindAgent(memberId, (String) map.get("agentId"), true);
			membersMsgService.updateMemberBindAgent(member);
			try {
				AccountingRecord rcd = memberGoldCmdService.giveGoldToMember(memberId, rights.getGoldForAgentInvite(),
						"bind invitioncode", System.currentTimeMillis());
				MemberGoldRecordDbo dbo = memberGoldQueryService.withdraw(memberId, rcd);
				// rcd发kafka
				goldsMsgService.withdraw(dbo);
			} catch (MemberNotFoundException e) {

			}
		}
		vo.setSuccess(commonRemoteVo.isSuccess());
		vo.setMsg(commonRemoteVo.getMsg());
		return vo;
	}

	@RequestMapping("/removeagent")
	public CommonVO removeagent(String memberId) {
		CommonVO vo = new CommonVO();
		MemberDbo member = memberAuthQueryService.removeMemberBindAgent(memberId);
		membersMsgService.removeMemberBindAgent(member);
		vo.setSuccess(true);
		return vo;
	}

	@RequestMapping("/updateagent")
	public CommonVO updateagent(String memberId, String agentId) {
		CommonVO vo = new CommonVO();
		// 查询MemberDbo的hasBindAgent是否为true(该用户是否绑定过)
		MemberDbo initialMemberDbo = memberAuthQueryService.findMemberById(memberId);

		// 如果hasBindAgent为false,赠送金币,
		if (!initialMemberDbo.isHasBindAgent()) {
			MemberRights rights = initialMemberDbo.getRights();
			try {
				AccountingRecord rcd = memberGoldCmdService.giveGoldToMember(memberId, rights.getGoldForAgentInvite(),
						"bind invitioncode", System.currentTimeMillis());
				MemberGoldRecordDbo dbo = memberGoldQueryService.withdraw(memberId, rcd);
				// rcd发kafka
				goldsMsgService.withdraw(dbo);
			} catch (MemberNotFoundException e) {

			}
		}
		// 设置hasBindAgent为true并设置bindAgent为true
		boolean hasBindAgent = true;
		MemberDbo member = memberAuthQueryService.updateMemberHasBindAgent(memberId, agentId, hasBindAgent);
		membersMsgService.addMemberBindAgent(member);
		vo.setSuccess(true);
		return vo;
	}

	@RequestMapping("/querygoldaccount")
	public CommonVO queryGoldAccount(@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "size", defaultValue = "10") Integer size, String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		ListPage listPage = memberGoldQueryService.findMemberGoldRecords(page, size, memberId);
		vo.setSuccess(true);
		vo.setMsg("goldaccout");
		vo.setData(listPage);
		return vo;
	}

	@RequestMapping("/queryscoreaccount")
	public CommonVO queryScoreAccount(@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "size", defaultValue = "10") Integer size, String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		ListPage listPage = memberScoreQueryService.findMemberScoreRecords(page, size, memberId);
		vo.setSuccess(true);
		vo.setMsg("scoreaccout");
		vo.setData(listPage);
		return vo;
	}

	@RequestMapping("/rechargevip")
	public CommonVO rechargeVip(String memberId, Long vipEndTime) {
		CommonVO vo = new CommonVO();
		MemberDbo member = memberAuthQueryService.rechargeVip(memberId, vipEndTime);
		membersMsgService.rechargeVip(member);
		vo.setSuccess(true);
		vo.setMsg("success");
		return vo;
	}


	@RequestMapping("/update_viptime")
	public CommonVO update_viptime(@RequestBody String[] ids, Long vipEndTime) {
		CommonVO vo = new CommonVO();
		for (String id : ids) {
			MemberDbo member = memberAuthQueryService.rechargeVip(id, vipEndTime);
			membersMsgService.rechargeVip(member);
		}
		vo.setSuccess(true);
		vo.setMsg("success");
		return vo;
	}

	/**
	 * 会员到期判定
	 */
	@Scheduled(cron = "0 20 0 * * ?") // 每天凌晨20分刷新会员
	@RequestMapping("/resetvip")
	public void resetVIP() {
		int size = 2000;
		long amount = memberAuthQueryService.getAmountByVip(true);
		long pageCount = amount % size > 0 ? amount / size + 1 : amount / size;
		for (int page = 1; page <= pageCount; page++) {
			List<MemberDbo> memberList = memberAuthQueryService.findMemberByVip(page, size, true);
			for (MemberDbo member : memberList) {
				if (member.getVipEndTime() < System.currentTimeMillis()) {
					MemberDbo memberDbo = memberAuthQueryService.updateMemberVip(member.getId(), false);
					// kafka更新
					membersMsgService.updateMemberVip(memberDbo);
				}
			}
		}
	}

	/**
	 * 刷新vip
	 */
	@RequestMapping("/flushvip")
	public void flushVIP() {
		int size = 2000;
		long amount = memberAuthQueryService.getAmountByVip(true);
		long pageCount = amount % size > 0 ? amount / size + 1 : amount / size;
		for (int page = 1; page <= pageCount; page++) {
			List<MemberDbo> memberList = memberAuthQueryService.findMemberByVip(page, size, true);
			for (MemberDbo member : memberList) {
				// kafka更新
				membersMsgService.updateMemberVip(member);
			}
		}
	}

}
