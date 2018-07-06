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
import com.anbang.qipai.members.util.TimeUtil;
import com.anbang.qipai.members.web.vo.CommonVO;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;

/**邮件奖励controller
 * @author 程佳  2018.6.20
 * **/
@RestController
@RequestMapping("/reward")
public class MemberReward {
	
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
	public CommonVO mail_Reward(String memberId,Integer number,Integer integral,Integer vipcard) throws InsufficientBalanceException, MemberNotFoundException {
		if(memberId != null) {
			MemberDbo memberDbo = memberService.findMemberById(memberId);
			if(number != null) {
				memberDbo.setGold(memberDbo.getGold()+number);
				AccountingRecord goldrcd = 	memberGoldCmdService.giveGoldToMember(memberId, number, "mail_reward", System.currentTimeMillis());
				//添加金币流水
				MemberGoldRecordDbo golddbo = memberGoldQueryService.withdraw(memberId, goldrcd);
				goldsMsgService.withdraw(golddbo);
			}
			if(integral != null) {
				memberDbo.setScore(memberDbo.getScore()+integral);
				AccountingRecord scorercd = memberScoreCmdService.giveScoreToMember(memberId, integral, "mail_reward", System.currentTimeMillis());
				//添加积分流水
				MemberScoreRecordDbo scoredbo = memberScoreQueryService.withdraw(memberId, scorercd);
				scoresMsgService.withdraw(scoredbo);
			}
			if(vipcard != null) {
				long time = TimeUtil.getTimeOnDay(vipcard);
				memberDbo.setVipEndTime(memberDbo.getVipEndTime()+time);
			}
			membersMsgService.updateMember(memberDbo);
		}
		return new CommonVO(); 
	}
	
	@RequestMapping("/task_reward")
	@ResponseBody
	public CommonVO task_reward(String rewardType,Integer rewardNum,String memberId) throws MemberNotFoundException {
		if(rewardType != null && rewardNum != null && memberId != null) {
			MemberDbo memberDbo = memberService.findMemberById(memberId);
			if("金币".equals(rewardType)) {
				int gold = rewardNum * 10000;
				memberDbo.setGold(memberDbo.getGold()+gold);
				AccountingRecord goldrcd = memberGoldCmdService.giveGoldToMember(memberId, gold, "task_reward", System.currentTimeMillis());
				//添加流水
				MemberGoldRecordDbo golddbo = memberGoldQueryService.withdraw(memberId, goldrcd);
				goldsMsgService.withdraw(golddbo);
			}
			if("积分".equals(rewardType)) {
				memberDbo.setScore(memberDbo.getScore()+rewardNum);
				AccountingRecord scorercd = memberScoreCmdService.giveScoreToMember(memberId, rewardNum, "task_reward", System.currentTimeMillis());
				//添加流水
				MemberScoreRecordDbo scoredbo = memberScoreQueryService.withdraw(memberId, scorercd);
				scoresMsgService.withdraw(scoredbo);
			}
			if("会员卡".equals(rewardType)) {
				long time = TimeUtil.getTimeOnDay(rewardNum);
				memberDbo.setVipEndTime(memberDbo.getVipEndTime()+time);
			}
			membersMsgService.updateMember(memberDbo);
		}
		return new CommonVO();
	}
	
}
