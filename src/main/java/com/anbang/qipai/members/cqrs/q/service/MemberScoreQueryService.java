package com.anbang.qipai.members.cqrs.q.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.cqrs.c.domain.CreateMemberResult;
import com.anbang.qipai.members.cqrs.q.dao.MemberScoreAccountDboDao;
import com.anbang.qipai.members.cqrs.q.dao.MemberScoreRecordDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreAccountDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreRecordDbo;
import com.dml.accounting.AccountingRecord;
import com.highto.framework.web.page.ListPage;

@Service
public class MemberScoreQueryService {
	@Autowired
	private MemberScoreRecordDboDao memberScoreRecordDboDao;

	@Autowired
	private MemberScoreAccountDboDao memberScoreAccountDboDao;

	public void recordMemberScoreRecord(AccountingRecord accountingRecord) {
		MemberScoreRecordDbo dbo = new MemberScoreRecordDbo();
		dbo.setAccountId(accountingRecord.getAccountId());
		dbo.setAccountingAmount((int) accountingRecord.getAccountingAmount());
		dbo.setAccountingNo(accountingRecord.getAccountingNo());
		dbo.setBalanceAfter((int) accountingRecord.getBalanceAfter());
		memberScoreRecordDboDao.save(dbo);
	}

	public void createMember(CreateMemberResult createMemberResult) {
		MemberScoreAccountDbo account = new MemberScoreAccountDbo();
		account.setId(createMemberResult.getScoreAccountId());
		account.setMemberId(createMemberResult.getMemberId());
		memberScoreAccountDboDao.save(account);

		AccountingRecord accountingRecord = createMemberResult.getAccountingRecordForGiveScore();
		MemberScoreRecordDbo dbo = new MemberScoreRecordDbo();
		dbo.setAccountId(accountingRecord.getAccountId());
		dbo.setAccountingAmount((int) accountingRecord.getAccountingAmount());
		dbo.setAccountingNo(accountingRecord.getAccountingNo());
		dbo.setBalanceAfter((int) accountingRecord.getBalanceAfter());
		dbo.setSummary(accountingRecord.getSummary());
		dbo.setAccountingTime(accountingRecord.getAccountingTime());
		memberScoreRecordDboDao.save(dbo);

		memberScoreAccountDboDao.update(account.getId(), (int) accountingRecord.getBalanceAfter());
	}

	public MemberScoreAccountDbo findMemberScoreAccount(String memberId) {
		return memberScoreAccountDboDao.findByMemberId(memberId);
	}

	public ListPage findMemberScoreRecords(int page, int size, String accountId) {
		PageRequest pageRequest = new PageRequest(page - 1, size);
		List<MemberScoreRecordDbo> recordList = memberScoreRecordDboDao.findMemberScoreRecords(accountId, pageRequest);
		long amount = memberScoreRecordDboDao.getCount();
		long pageNum = (amount == 0) ? 1 : ((amount % size == 0) ? (amount / size) : (amount / size + 1));
		ListPage listPage = new ListPage(recordList, (int) pageNum, size, (int) amount);
		return listPage;
	}

	public MemberScoreRecordDbo withdraw(String memberId, AccountingRecord accountingRecord) {

		MemberScoreRecordDbo dbo = new MemberScoreRecordDbo();
		dbo.setMemberId(memberId);
		dbo.setAccountId(accountingRecord.getAccountId());
		dbo.setAccountingAmount((int) accountingRecord.getAccountingAmount());
		dbo.setAccountingNo(accountingRecord.getAccountingNo());
		dbo.setBalanceAfter((int) accountingRecord.getBalanceAfter());
		dbo.setSummary(accountingRecord.getSummary());
		dbo.setAccountingTime(accountingRecord.getAccountingTime());
		memberScoreRecordDboDao.save(dbo);

		memberScoreAccountDboDao.update(accountingRecord.getAccountId(), (int) accountingRecord.getBalanceAfter());
		return dbo;
	}
}
