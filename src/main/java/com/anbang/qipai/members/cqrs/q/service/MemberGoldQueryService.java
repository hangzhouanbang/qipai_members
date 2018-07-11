package com.anbang.qipai.members.cqrs.q.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.c.domain.CreateMemberResult;
import com.anbang.qipai.members.cqrs.q.dao.MemberDboDao;
import com.anbang.qipai.members.cqrs.q.dao.MemberGoldAccountDboDao;
import com.anbang.qipai.members.cqrs.q.dao.MemberGoldRecordDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldAccountDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldRecordDbo;
import com.dml.accounting.AccountingRecord;
import com.highto.framework.web.page.ListPage;

@Component
public class MemberGoldQueryService {

	@Autowired
	private MemberGoldRecordDboDao memberGoldRecordDboDao;

	@Autowired
	private MemberGoldAccountDboDao memberGoldAccountDboDao;

	@Autowired
	private MemberDboDao memberDboDao;

	public void recordMemberGoldRecord(AccountingRecord accountingRecord) {
		MemberGoldRecordDbo dbo = new MemberGoldRecordDbo();
		dbo.setAccountId(accountingRecord.getAccountId());
		dbo.setAccountingAmount((int) accountingRecord.getAccountingAmount());
		dbo.setAccountingNo(accountingRecord.getAccountingNo());
		dbo.setBalanceAfter((int) accountingRecord.getBalanceAfter());
		memberGoldRecordDboDao.save(dbo);
	}

	public MemberGoldRecordDbo createMember(CreateMemberResult createMemberResult) {
		MemberGoldAccountDbo account = new MemberGoldAccountDbo();
		account.setId(createMemberResult.getGoldAccountId());
		account.setMemberId(createMemberResult.getMemberId());
		memberGoldAccountDboDao.save(account);

		AccountingRecord accountingRecord = createMemberResult.getAccountingRecordForGiveGold();
		MemberGoldRecordDbo dbo = new MemberGoldRecordDbo();
		dbo.setMemberId(createMemberResult.getMemberId());
		dbo.setAccountId(accountingRecord.getAccountId());
		dbo.setAccountingAmount((int) accountingRecord.getAccountingAmount());
		dbo.setAccountingNo(accountingRecord.getAccountingNo());
		dbo.setBalanceAfter((int) accountingRecord.getBalanceAfter());
		dbo.setSummary(accountingRecord.getSummary());
		dbo.setAccountingTime(accountingRecord.getAccountingTime());
		memberGoldRecordDboDao.save(dbo);

		memberGoldAccountDboDao.update(account.getId(), (int) accountingRecord.getBalanceAfter());
		return dbo;
	}

	public MemberGoldAccountDbo findMemberGoldAccount(String memberId) {
		return memberGoldAccountDboDao.findByMemberId(memberId);
	}

	public ListPage findMemberGoldRecords(int page, int size, String accountId) {
		PageRequest pageRequest = new PageRequest(page - 1, size);
		List<MemberGoldRecordDbo> recordList = memberGoldRecordDboDao.findMemberGoldRecords(accountId, pageRequest);
		long amount = memberGoldRecordDboDao.getCount();
		long pageNum = (amount == 0) ? 1 : ((amount % size == 0) ? (amount / size) : (amount / size + 1));
		ListPage listPage = new ListPage(recordList, (int) pageNum, size, (int) amount);
		return listPage;
	}

	public MemberGoldRecordDbo withdraw(String memberId, AccountingRecord accountingRecord) {

		MemberGoldRecordDbo dbo = new MemberGoldRecordDbo();
		dbo.setMemberId(memberId);
		dbo.setAccountId(accountingRecord.getAccountId());
		dbo.setAccountingAmount((int) accountingRecord.getAccountingAmount());
		dbo.setAccountingNo(accountingRecord.getAccountingNo());
		dbo.setBalanceAfter((int) accountingRecord.getBalanceAfter());
		dbo.setSummary(accountingRecord.getSummary());
		dbo.setAccountingTime(accountingRecord.getAccountingTime());
		memberGoldRecordDboDao.save(dbo);
		MemberDbo member = memberDboDao.findById(memberId);

		memberGoldAccountDboDao.update(accountingRecord.getAccountId(), (int) accountingRecord.getBalanceAfter());
		return dbo;
	}
}
