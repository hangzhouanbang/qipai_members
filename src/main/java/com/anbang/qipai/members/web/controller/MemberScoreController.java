package com.anbang.qipai.members.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.service.MemberScoreCmdService;
import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreRecordDbo;
import com.anbang.qipai.members.cqrs.q.service.MemberScoreQueryService;
import com.anbang.qipai.members.msg.service.ScoresMsgService;
import com.anbang.qipai.members.web.vo.CommonVO;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;

@CrossOrigin
@RestController
@RequestMapping("/score")
public class MemberScoreController {
	@Autowired
	private MemberScoreCmdService memberScoreCmdService;

	@Autowired
	private MemberScoreQueryService memberScoreQueryService;

	@Autowired
	private ScoresMsgService scoresMsgService;

	@RequestMapping(value = "/withdraw")
	@ResponseBody
	public CommonVO withdraw(String memberId, int amount, String textSummary) {
		CommonVO vo = new CommonVO();
		try {
			AccountingRecord rcd = memberScoreCmdService.withdraw(memberId, amount, textSummary,
					System.currentTimeMillis());
			MemberScoreRecordDbo dbo = memberScoreQueryService.withdraw(memberId, rcd);

			scoresMsgService.withdraw(dbo);
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

	@RequestMapping(value = "/givescoretomember")
	@ResponseBody
	public CommonVO giveScoreToMember(String memberId, int amount, String textSummary) {
		CommonVO vo = new CommonVO();
		try {
			AccountingRecord rcd = memberScoreCmdService.giveScoreToMember(memberId, amount, textSummary,
					System.currentTimeMillis());
			MemberScoreRecordDbo dbo = memberScoreQueryService.withdraw(memberId, rcd);

			scoresMsgService.withdraw(dbo);
			return vo;
		} catch (MemberNotFoundException e) {
			vo.setSuccess(false);
			vo.setMsg("MemberNotFoundException");
			return vo;
		}
	}
}
