package com.anbang.qipai.members.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
import com.anbang.qipai.members.web.vo.CommonVO;
import com.anbang.qipai.members.web.vo.DetailsVo;
import com.anbang.qipai.members.web.vo.MemberVO;
import com.dml.accounting.AccountingRecord;
import com.highto.framework.web.page.ListPage;

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

	@RequestMapping(value = "/info")
	@ResponseBody
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
		MemberGoldAccountDbo memberGoldAccountDbo = memberGoldQueryService.findMemberGoldAccount(memberId);
		if (memberGoldAccountDbo != null) {
			vo.setGold(memberGoldAccountDbo.getBalance());
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
		MemberDbo member = memberService.findMemberById(memberId);
		vo.setVipLevel(member.getVipLevel());
		vo.setPhone(member.getPhone());
		long time = member.getVipEndTime();
		long nowTime = System.currentTimeMillis();
		long day = (time - nowTime) / (1000 * 60 * 60 * 24);
		vo.setVipEndTime("剩余" + day + "天");
		vo.setSuccess(true);
		vo.setMsg("information");
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
		membersMsgService.updateMember(memberdbo);
		vo.setSuccess(true);
		vo.setMsg("register success");
		vo.setData(phone);
		return vo;
	}

	@RequestMapping("/querygoldaccount")
	public CommonVO queryGoldAccount(@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "size", defaultValue = "10") Integer size, String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId != null) {
			MemberGoldAccountDbo accountId = memberGoldQueryService.findMemberGoldAccount(memberId);
			ListPage listPage = memberGoldQueryService.findMemberGoldRecords(page, size, accountId.getId());
			// ListPage listPage = memberGoldQueryService.findMemberGoldRecords(page, size,
			// "627532_gold_wallet");// 测试用
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
			MemberScoreAccountDbo accountId = memberScoreQueryService.findMemberScoreAccount(memberId);
			ListPage listPage = memberScoreQueryService.findMemberScoreRecords(page, size, accountId.getId());
			// ListPage listPage = memberScoreQueryService.findMemberScoreRecords(page,
			// size, "627532_gold_wallet");// 测试用
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
	@ResponseBody
	public CommonVO update_score_gold(@RequestBody String[] ids, String score, String gold) {
		CommonVO vo = new CommonVO();
		vo.setSuccess(true);
		for (String id : ids) {
			try {
				if (score != null && !score.equals("") && score.matches("^[0-9]*$")) {
					// 添加积分
					AccountingRecord scorercd = memberScoreCmdService.giveScoreToMember(id, Integer.parseInt(score),
							"admin_give_score", System.currentTimeMillis());
					MemberScoreRecordDbo scoredbo = memberScoreQueryService.withdraw(id, scorercd);
					MemberDbo memberDbo = memberService.findMemberById(id);
					// kafka更新
					membersMsgService.updateMember(memberDbo);
					// TODO: rcd发kafka
					scoresMsgService.withdraw(scoredbo);
				}
				if (gold != null && !gold.equals("") && gold.matches("^[0-9]*$")) {
					// 添加金币
					AccountingRecord goldrcd = memberGoldCmdService.giveGoldToMember(id, Integer.parseInt(gold),
							"admin_give_gold", System.currentTimeMillis());
					MemberGoldRecordDbo golddbo = memberGoldQueryService.withdraw(id, goldrcd);
					MemberDbo memberDbo = memberService.findMemberById(id);
					// kafka更新
					membersMsgService.updateMember(memberDbo);
					// TODO: rcd发kafka
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

	/**
	 * 会员到期判定
	 * 
	 */
	@Scheduled(cron = "0 0 0 * * ?") // 每天凌晨
	public void resetVIP() {
		int size = 200;
		long amount = memberService.getAmount();
		long pageCount = amount / size > 0 ? amount / size + 1 : amount / size;
		for (int page = 1; page <= pageCount; page++) {
			List<MemberDbo> memberList = memberService.findMember(page, size);
			for (MemberDbo member : memberList) {
				if (member.getVipEndTime() < System.currentTimeMillis()) {
					member.setVip(false);
					memberService.resetVIP(member);
					// kafka更新
					membersMsgService.updateMember(member);
				}
			}
		}
	}
}
