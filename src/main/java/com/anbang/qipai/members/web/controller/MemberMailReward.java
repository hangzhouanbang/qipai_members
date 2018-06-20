package com.anbang.qipai.members.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.service.MemberGoldCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberScoreCmdService;
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
	
	@RequestMapping("/mail_reward")
	@ResponseBody
	public CommonVO mail_reward(String memberId,Integer number,Integer integral,Integer vipcard) throws InsufficientBalanceException, MemberNotFoundException {
		if(memberId != null) {
			if(number != null) {
				memberGoldCmdService.withdraw(memberId, number, "邮件奖励", System.currentTimeMillis());
			}
			if(integral != null) {
				memberScoreCmdService.giveScoreToMember(memberId, integral, "邮件奖励", System.currentTimeMillis());
			}
			if(vipcard != null) {
				
			}
		}
		return new CommonVO(); 
	}
}
