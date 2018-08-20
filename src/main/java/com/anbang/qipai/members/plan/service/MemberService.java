package com.anbang.qipai.members.plan.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.plan.bean.MemberGrade;
import com.anbang.qipai.members.plan.bean.MemberOrder;
import com.anbang.qipai.members.plan.dao.MemberDao;
import com.anbang.qipai.members.plan.dao.MemberGradeDao;

@Service
public class MemberService {

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private MemberGradeDao memberGradeDao;

	public MemberDbo findMemberById(String memberId) {
		return memberDao.findMemberById(memberId);
	}

	public long getAmount() {
		return memberDao.getAmount();
	}

	public List<MemberDbo> findMember(int page, int size) {
		return memberDao.findMember(page, size);
	}

	public void registerPhone(String memberId, String phone) {
		memberDao.updateMemberPhone(memberId, phone);
	}

	public boolean updateVipEndTime(String memberId, long vipEndTime) {
		return memberDao.updateMemberVipEndTime(memberId, vipEndTime);
	}

	public boolean updateVIP(MemberOrder order) {
		MemberDbo member = memberDao.findMemberById(order.getPayerId());
		member.setVip(true);
		long vipEndTime=0;
		if(member.getVipEndTime()!=null) {
			vipEndTime = member.getVipEndTime();
		}
		if (vipEndTime > System.currentTimeMillis()) {
			member.setVipEndTime(order.getVipTime() + vipEndTime);
		} else {
			member.setVipEndTime(order.getVipTime() + System.currentTimeMillis());
		}
		BigDecimal b1 = new BigDecimal(Double.toString(order.getProductPrice()));
		BigDecimal b2 = new BigDecimal(Double.toString(member.getVipScore()));
		double vipScore = b1.add(b2).doubleValue();
		member.setVipScore(vipScore);
		double cost = order.getProductPrice() + member.getCost();
		member.setCost(cost);
		MemberGrade grade = memberGradeDao.find_grade("1");
		if (grade != null) {
			int level = member.getVipLevel();
			long score = grade.getLevel(level);
			if (vipScore >= score) {
				member.setVipLevel(level + 1);
			}
		}
		return memberDao.updateMemberVIP(member);
	}

	public boolean resetVIP(MemberDbo member) {
		return memberDao.resetVip(member);
	}
}
