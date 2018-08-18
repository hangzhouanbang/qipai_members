package com.anbang.qipai.members.plan.service;

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
		member.setVipEndTime(order.getVipTime() + System.currentTimeMillis());
		int vipScore = (int) (order.getProductPrice() * 100) + member.getVipScore();
		member.setVipScore(vipScore);
		double cost = order.getProductPrice() + member.getCost();
		member.setCost(cost);
		MemberGrade grade = memberGradeDao.find_grade("1");
		if(grade!=null) {
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
