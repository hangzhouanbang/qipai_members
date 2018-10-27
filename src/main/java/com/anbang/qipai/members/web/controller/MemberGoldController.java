package com.anbang.qipai.members.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.service.MemberGoldCmdService;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldRecordDbo;
import com.anbang.qipai.members.cqrs.q.service.MemberGoldQueryService;
import com.anbang.qipai.members.msg.service.GoldsMsgService;
import com.anbang.qipai.members.web.vo.CommonVO;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;

@CrossOrigin
@RestController
@RequestMapping("/gold")
public class MemberGoldController {

	@Autowired
	private MemberGoldCmdService memberGoldCmdService;

	@Autowired
	private MemberGoldQueryService memberGoldQueryService;

	@Autowired
	private GoldsMsgService goldsMsgService;

	@RequestMapping(value = "/withdraw")
	@ResponseBody
	public CommonVO withdraw(String memberId, int amount, String textSummary) {
		CommonVO vo = new CommonVO();
		try {
			AccountingRecord rcd = memberGoldCmdService.withdraw(memberId, amount, textSummary,
					System.currentTimeMillis());
			MemberGoldRecordDbo dbo = memberGoldQueryService.withdraw(memberId, rcd);
			// rcd发kafka
			goldsMsgService.withdraw(dbo);
			return vo;
		} catch (InsufficientBalanceException e) {
			vo.setSuccess(false);
			vo.setMsg("InsufficientBalanceException");
			return vo;
		} catch (MemberNotFoundException e) {
			vo.setSuccess(false);
			vo.setMsg("MemberNotFoundException");
			return vo;
		}
	}

	@RequestMapping(value = "/givegoldtomember")
	public CommonVO giveGoldToMember(String memberId, int amount, String textSummary) {
		CommonVO vo = new CommonVO();
		try {
			AccountingRecord rcd = memberGoldCmdService.giveGoldToMember(memberId, amount, textSummary,
					System.currentTimeMillis());
			MemberGoldRecordDbo dbo = memberGoldQueryService.withdraw(memberId, rcd);
			// rcd发kafka
			goldsMsgService.withdraw(dbo);
			return vo;
		} catch (MemberNotFoundException e) {
			vo.setSuccess(false);
			vo.setMsg("MemberNotFoundException");
			return vo;
		}
	}

	@RequestMapping(value = "/members_withdraw")
	@ResponseBody
	public CommonVO membersWithdraw(@RequestBody String[] memberIds, int amount, String textSummary) {
		CommonVO vo = new CommonVO();
		try {
			for (String memberId : memberIds) {
				AccountingRecord rcd = memberGoldCmdService.withdraw(memberId, amount, textSummary,
						System.currentTimeMillis());
				MemberGoldRecordDbo dbo = memberGoldQueryService.withdraw(memberId, rcd);
				// rcd发kafka
				goldsMsgService.withdraw(dbo);
			}
		} catch (InsufficientBalanceException e) {

		} catch (MemberNotFoundException e) {

		}
		vo.setSuccess(true);
		return vo;
	}

	@RequestMapping(value = "/givegoldtomembers")
	public CommonVO giveGoldToMembers(@RequestBody String[] memberIds, int amount, String textSummary) {
		CommonVO vo = new CommonVO();
		try {
			for (String memberId : memberIds) {
				AccountingRecord rcd = memberGoldCmdService.giveGoldToMember(memberId, amount, textSummary,
						System.currentTimeMillis());
				MemberGoldRecordDbo dbo = memberGoldQueryService.withdraw(memberId, rcd);
				// rcd发kafka
				goldsMsgService.withdraw(dbo);
			}
		} catch (MemberNotFoundException e) {
		}
		vo.setSuccess(true);
		return vo;
	}
}
