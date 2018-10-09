package com.anbang.qipai.members.cqrs.q.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.cqrs.c.domain.CreateMemberResult;
import com.anbang.qipai.members.cqrs.q.dao.MemberDboDao;
import com.anbang.qipai.members.cqrs.q.dao.MemberScoreAccountDboDao;
import com.anbang.qipai.members.cqrs.q.dao.MemberScoreRecordDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreAccountDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreRecordDbo;
import com.anbang.qipai.members.web.vo.RecordSummaryTexts;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.TextAccountingSummary;
import com.highto.framework.web.page.ListPage;

@Service
public class MemberScoreQueryService {
	@Autowired
	private MemberScoreRecordDboDao memberScoreRecordDboDao;

	@Autowired
	private MemberScoreAccountDboDao memberScoreAccountDboDao;

	@Autowired
	private MemberDboDao memberDboDao;

	public void recordMemberScoreRecord(AccountingRecord accountingRecord) {
		MemberScoreRecordDbo dbo = new MemberScoreRecordDbo();
		dbo.setAccountId(accountingRecord.getAccountId());
		dbo.setAccountingAmount((int) accountingRecord.getAccountingAmount());
		dbo.setAccountingNo(accountingRecord.getAccountingNo());
		dbo.setBalanceAfter((int) accountingRecord.getBalanceAfter());
		memberScoreRecordDboDao.save(dbo);
	}

	public MemberScoreRecordDbo createMember(CreateMemberResult createMemberResult) {
		MemberScoreAccountDbo account = new MemberScoreAccountDbo();
		account.setId(createMemberResult.getScoreAccountId());
		account.setMemberId(createMemberResult.getMemberId());
		memberScoreAccountDboDao.save(account);

		AccountingRecord accountingRecord = createMemberResult.getAccountingRecordForGiveScore();
		MemberScoreRecordDbo dbo = new MemberScoreRecordDbo();
		dbo.setMemberId(createMemberResult.getMemberId());
		dbo.setAccountId(accountingRecord.getAccountId());
		dbo.setAccountingAmount((int) accountingRecord.getAccountingAmount());
		dbo.setAccountingNo(accountingRecord.getAccountingNo());
		dbo.setBalanceAfter((int) accountingRecord.getBalanceAfter());
		dbo.setSummary(accountingRecord.getSummary());
		dbo.setAccountingTime(accountingRecord.getAccountingTime());
		memberScoreRecordDboDao.save(dbo);

		memberScoreAccountDboDao.update(account.getId(), (int) accountingRecord.getBalanceAfter());
		return dbo;
	}

	public MemberScoreAccountDbo findMemberScoreAccount(String memberId) {
		return memberScoreAccountDboDao.findByMemberId(memberId);
	}

	public ListPage findMemberScoreRecords(int page, int size, String memberId) {
		List<MemberScoreRecordDbo> recordList = memberScoreRecordDboDao.findMemberScoreRecordByMemberId(memberId, page,
				size);
		for (int i = 0; i < recordList.size(); i++) {
			TextAccountingSummary summary = (TextAccountingSummary) recordList.get(i).getSummary();
			TextAccountingSummary newSummary = new TextAccountingSummary(
					RecordSummaryTexts.getSummaryText(summary.getText()));
			recordList.get(i).setSummary(newSummary);
		}
		long amount = memberScoreRecordDboDao.getCountByMemberId(memberId);
		ListPage listPage = new ListPage(recordList, page, size, (int) amount);
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
		memberDboDao.updateMemberScore(memberId, (int) accountingRecord.getBalanceAfter());
		return dbo;
	}
}
