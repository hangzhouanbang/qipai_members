package com.anbang.qipai.members.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.service.MemberGoldCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberScoreCmdService;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldRecordDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreRecordDbo;
import com.anbang.qipai.members.cqrs.q.service.MemberGoldQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberScoreQueryService;
import com.anbang.qipai.members.msg.service.GoldsMsgService;
import com.anbang.qipai.members.msg.service.MembersMsgService;
import com.anbang.qipai.members.msg.service.ScoresMsgService;
import com.anbang.qipai.members.plan.service.MemberService;
import com.anbang.qipai.members.web.vo.CommonVO;
import com.dml.accounting.AccountingRecord;

/**
 * 邮件奖励controller
 * 
 * @author 程佳 2018.6.20
 **/
@RestController
@RequestMapping("/reward")
public class MemberRewardController {

	@Autowired
	private MemberGoldCmdService memberGoldCmdService;

	@Autowired
	private MemberScoreCmdService memberScoreCmdService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private ScoresMsgService scoresMsgService;

	@Autowired
	private GoldsMsgService goldsMsgService;

	@Autowired
	private MemberGoldQueryService memberGoldQueryService;

	@Autowired
	private MemberScoreQueryService memberScoreQueryService;

	@Autowired
	private MembersMsgService membersMsgService;

	@RequestMapping("/mail_reward")
	@ResponseBody
	public CommonVO mail_Reward(String memberId, Integer number, Integer integral, Integer vipcard) {
		CommonVO vo = new CommonVO();
		vo.setSuccess(true);
		try {
			if (number != null) {
				AccountingRecord goldrcd = memberGoldCmdService.giveGoldToMember(memberId, number, "mail_reward",
						System.currentTimeMillis());
				// 添加金币流水
				MemberGoldRecordDbo golddbo = memberGoldQueryService.withdraw(memberId, goldrcd);
				goldsMsgService.withdraw(golddbo);
			}
			if (integral != null) {
				AccountingRecord scorercd = memberScoreCmdService.giveScoreToMember(memberId, integral, "mail_reward",
						System.currentTimeMillis());
				// 添加积分流水
				MemberScoreRecordDbo scoredbo = memberScoreQueryService.withdraw(memberId, scorercd);
				scoresMsgService.withdraw(scoredbo);
			}
		} catch (MemberNotFoundException e) {
			vo.setSuccess(false);
			vo.setMsg("member not found");
			e.printStackTrace();
		}
		if (vipcard != null) {
			long time = 1000 * 60 * 60 * 24 * vipcard;
			memberService.updateVipEndTime(memberId, System.currentTimeMillis() + time);
			MemberDbo member = memberService.findMemberById(memberId);
			membersMsgService.updateMemberVip(member);
		}
		return vo;
	}

	@RequestMapping("/task_reward")
	@ResponseBody
	public CommonVO task_reward(Integer rewardGold, Integer rewardScore, Integer rewardVip, String memberId)
			throws MemberNotFoundException {
		CommonVO vo = new CommonVO();
		vo.setSuccess(true);
		try {
			if (rewardGold != null) {
				int gold = rewardGold * 10000;
				AccountingRecord goldrcd = memberGoldCmdService.giveGoldToMember(memberId, gold, "task_reward",
						System.currentTimeMillis());
				// 添加流水
				MemberGoldRecordDbo golddbo = memberGoldQueryService.withdraw(memberId, goldrcd);
				goldsMsgService.withdraw(golddbo);
			}
			if (rewardScore != null) {
				AccountingRecord scorercd = memberScoreCmdService.giveScoreToMember(memberId, rewardScore,
						"task_reward", System.currentTimeMillis());
				// 添加流水
				MemberScoreRecordDbo scoredbo = memberScoreQueryService.withdraw(memberId, scorercd);
				scoresMsgService.withdraw(scoredbo);
			}
			if (rewardVip != null) {
				long time = 1000 * 60 * 60 * 24 * rewardVip;
				memberService.updateVipEndTime(memberId, System.currentTimeMillis() + time);
				MemberDbo member = memberService.findMemberById(memberId);
				membersMsgService.updateMemberVip(member);
			}
		} catch (MemberNotFoundException e) {
			vo.setSuccess(false);
			vo.setMsg("member not found");
			e.printStackTrace();
		}
		return vo;
	}

}
