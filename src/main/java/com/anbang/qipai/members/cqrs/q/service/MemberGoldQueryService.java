package com.anbang.qipai.members.cqrs.q.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.c.domain.CreateMemberResult;
import com.anbang.qipai.members.cqrs.q.dao.MemberDboDao;
import com.anbang.qipai.members.cqrs.q.dao.MemberGoldAccountDboDao;
import com.anbang.qipai.members.cqrs.q.dao.MemberGoldRecordDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldAccountDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldRecordDbo;
import com.anbang.qipai.members.web.vo.RecordSummaryTexts;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.TextAccountingSummary;
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
		memberDboDao.updateMemberGold(createMemberResult.getMemberId(), (int) accountingRecord.getBalanceAfter());
		return dbo;
	}

	public MemberGoldAccountDbo findMemberGoldAccount(String memberId) {
		return memberGoldAccountDboDao.findByMemberId(memberId);
	}

	public ListPage findMemberGoldRecords(int page, int size, String memberId) {
		List<MemberGoldRecordDbo> recordList = memberGoldRecordDboDao.findMemberGoldRecordByMemberId(memberId, page,
				size);
		for (int i = 0; i < recordList.size(); i++) {
			TextAccountingSummary summary = (TextAccountingSummary) recordList.get(i).getSummary();
			TextAccountingSummary newSummary = new TextAccountingSummary(
					RecordSummaryTexts.getSummaryText(summary.getText()));
			recordList.get(i).setSummary(newSummary);
		}
		long amount = memberGoldRecordDboDao.getCountByMemberId(memberId);
		ListPage listPage = new ListPage(recordList, page, size, (int) amount);
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

		memberGoldAccountDboDao.update(accountingRecord.getAccountId(), (int) accountingRecord.getBalanceAfter());
		memberDboDao.updateMemberGold(memberId, (int) accountingRecord.getBalanceAfter());
		return dbo;
	}
}
