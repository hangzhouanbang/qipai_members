package com.anbang.qipai.members.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.service.MemberGoldCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberScoreCmdService;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.msg.service.MembersMsgService;
import com.anbang.qipai.members.plan.service.MemberService;
import com.anbang.qipai.members.web.vo.CommonVO;
import com.dml.accounting.InsufficientBalanceException;

/**邮件奖励controller
 * @author 程佳  2018.6.20
 * **/
@RestController
@RequestMapping("/game")
public class MemberMailReward {
	
	@Autowired
	private MemberGoldCmdService memberGoldCmdService;
	
	@Autowired
	private MemberScoreCmdService memberScoreCmdService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MembersMsgService membersMsgService;
	
	@RequestMapping("/mail_reward")
	@ResponseBody
	public CommonVO mail_Reward(String memberId,Integer number,Integer integral,Integer vipcard) throws InsufficientBalanceException, MemberNotFoundException {
		if(memberId != null) {
			MemberDbo memberDbo = memberService.findMemberById(memberId);
			if(number != null) {
				memberDbo.setGold(memberDbo.getGold()+number);
				memberGoldCmdService.giveGoldToMember(memberId, number, "mail_reward", System.currentTimeMillis());
			}
			if(integral != null) {
				memberDbo.setScore(memberDbo.getScore()+integral);
				memberScoreCmdService.giveScoreToMember(memberId, integral, "mail_reward", System.currentTimeMillis());
			}
			if(vipcard != null) {
				
			}
			membersMsgService.updateMember(memberDbo);
		}
		return new CommonVO(); 
	}
}
