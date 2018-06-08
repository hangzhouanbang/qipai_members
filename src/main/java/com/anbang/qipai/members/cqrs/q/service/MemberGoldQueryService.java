package com.anbang.qipai.members.cqrs.q.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
		dbo.setSummary(accountingRecord.getSummary());
		dbo.setAccountingTime(accountingRecord.getAccountingTime());
		memberGoldRecordDboDao.save(dbo);

		memberGoldAccountDboDao.update(account.getId(), (int) accountingRecord.getBalanceAfter());
	}

	public MemberGoldAccountDbo findMemberGoldAccount(String memberId) {
		return memberGoldAccountDboDao.findByMemberId(memberId);
	}

	public Map<String, Object> findMemberGoldRecords(int page, int size, String accountId) {
		PageRequest pageRequest = new PageRequest(page, size);
		Map<String, Object> map = new HashMap<String, Object>();
		List<MemberGoldRecordDbo> recordList = memberGoldRecordDboDao.findMemberGoldRecords(accountId, pageRequest);
		long amount = memberGoldRecordDboDao.getCount();
		long pageNumber = (amount == 0) ? 1 : ((amount % size == 0) ? (amount / size) : (amount / size + 1));
		map.put("pageNumber", pageNumber);
		map.put("recordList", recordList);
		return map;
	}
}
