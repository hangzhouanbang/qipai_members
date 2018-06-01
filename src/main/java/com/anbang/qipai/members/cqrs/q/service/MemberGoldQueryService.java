package com.anbang.qipai.members.cqrs.q.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.c.domain.CreateMemberResult;
import com.anbang.qipai.members.cqrs.q.dao.MemberGoldAccountDboDao;
import com.anbang.qipai.members.cqrs.q.dao.MemberGoldRecordDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldAccountDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldRecordDbo;
import com.dml.accounting.AccountingRecord;

@Component
public class MemberGoldQueryService {

	@Autowired
	private MemberGoldRecordDboDao memberGoldRecordDboDao;

	@Autowired
	private MemberGoldAccountDboDao memberGoldAccountDboDao;

	public void recordMemberGoldRecord(AccountingRecord accountingRecord) {
		MemberGoldRecordDbo dbo = new MemberGoldRecordDbo();
		dbo.setAccountId(accountingRecord.getAccountId());
		dbo.setAccountingAmount((int) accountingRecord.getAccountingAmount());
		dbo.setAccountingNo(accountingRecord.getAccountingNo());
		dbo.setBalanceAfter((int) accountingRecord.getBalanceAfter());
		memberGoldRecordDboDao.save(dbo);
	}

	public void createMember(CreateMemberResult createMemberResult) {
		MemberGoldAccountDbo account = new MemberGoldAccountDbo();
		account.setId(createMemberResult.getGoldAccountId());
		account.setMemberId(createMemberResult.getMemberId());
		memberGoldAccountDboDao.save(account);

		AccountingRecord accountingRecord = createMemberResult.getAccountingRecordForGiveGold();
		MemberGoldRecordDbo dbo = new MemberGoldRecordDbo();
		dbo.setAccountId(accountingRecord.getAccountId());
		dbo.setAccountingAmount((int) accountingRecord.getAccountingAmount());
		dbo.setAccountingNo(accountingRecord.getAccountingNo());
		dbo.setBalanceAfter((int) accountingRecord.getBalanceAfter());
		memberGoldRecordDboDao.save(dbo);

		memberGoldAccountDboDao.update(account.getId(), (int) accountingRecord.getBalanceAfter());
	}

	public MemberGoldAccountDbo findMemberGoldAccount(String memberId) {
		return memberGoldAccountDboDao.findByMemberId(memberId);
	}

}
