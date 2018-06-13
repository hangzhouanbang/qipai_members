package com.anbang.qipai.members.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.domain.gold.MemberGoldAccountManager;
import com.anbang.qipai.members.cqrs.q.dao.MemberGoldAccountDboDao;
import com.anbang.qipai.members.cqrs.q.dao.MemberGoldRecordDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldRecordDbo;
import com.anbang.qipai.members.plan.dao.MemberDao;
import com.anbang.qipai.members.plan.dao.OrderDao;
import com.anbang.qipai.members.plan.domain.Order;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.TextAccountingSummary;
import com.highto.framework.ddd.SingletonEntityRepository;

@Service
public class MemberService {

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private MemberGoldRecordDboDao memberGoldRecordDboDao;

	@Autowired
	private MemberGoldAccountDboDao memberGoldAccountDboDao;

	@Autowired
	protected SingletonEntityRepository singletonEntityRepository;

	public MemberDbo findMemberById(String memberId) {
		return memberDao.findMemberById(memberId);
	}

	public void registerPhone(String memberId, String phone) {
		memberDao.updateMemberPhone(memberId, phone);
	}

	public void deliver(String out_trade_no, long currentTime) {
		Order order = orderDao.findOrderByOut_trade_no(out_trade_no);
		if (order.getStatus() == 0) {
			MemberDbo member = memberDao.findMemberById(order.getMemberId());
			long vipEndTime = member.getVipEndTime() + order.getVipTime() * order.getNumber();
			memberDao.updateMemberVIP(order.getMemberId(), vipEndTime);

			MemberGoldAccountManager memberGoldAccountManager = singletonEntityRepository
					.getEntity(MemberGoldAccountManager.class);
			AccountingRecord accountingRecord = null;
			try {
				accountingRecord = memberGoldAccountManager.giveGoldToMember(order.getMemberId(),
						order.getGold() * order.getNumber(), new TextAccountingSummary("buy" + order.getClubCardName()),
						currentTime);
			} catch (MemberNotFoundException e) {
				e.printStackTrace();
			}
			MemberGoldRecordDbo dbo = new MemberGoldRecordDbo();
			dbo.setAccountId(accountingRecord.getAccountId());
			dbo.setAccountingAmount((int) accountingRecord.getAccountingAmount());
			dbo.setAccountingNo(accountingRecord.getAccountingNo());
			dbo.setBalanceAfter((int) accountingRecord.getBalanceAfter());
			dbo.setSummary(accountingRecord.getSummary());
			dbo.setAccountingTime(accountingRecord.getAccountingTime());
			memberGoldRecordDboDao.save(dbo);
			memberGoldAccountDboDao.update(accountingRecord.getAccountId(), (int) accountingRecord.getBalanceAfter());

			orderDao.updateOrderStatus(out_trade_no, 1);
		}
	}
}
