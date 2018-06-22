package com.anbang.qipai.members.plan.service;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.cqrs.q.dao.MemberRechargeRecordDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.MemberRechargeRecordDbo;
import com.anbang.qipai.members.plan.dao.MemberGradeDao;
import com.anbang.qipai.members.plan.domain.MemberGrade;

@Service
public class MemberGradeService {
	
	@Autowired
	private MemberRechargeRecordDboDao memberRechargeRecordDboDao;
	
	@Autowired
	private MemberGradeDao memberGradeDao;
	
	/**添加会员各个等级信息
	 * **/
	public void insert_grade(MemberGrade memberGrade) {
		memberGrade.setId("1");
		memberGradeDao.insert_grade(memberGrade);
	}
	
	/**查询会员各个等级信息
	 * **/
	public MemberGrade find_grade() {
		return memberGradeDao.find_grade("1");
	}
	/**查询会员等级经验
	 * **/
	public MemberRechargeRecordDbo find_Recharge_Record(String memberId) {
		MemberRechargeRecordDbo memberRechargeRecordDbo = memberRechargeRecordDboDao.find_grade(memberId);
		MemberGrade memberGrade = new MemberGrade();
		if(memberRechargeRecordDbo == null) {
			memberRechargeRecordDbo.setMemberId(memberId);
			memberRechargeRecordDbo.setMemberGrade("vip0");
			memberRechargeRecordDbo.setRechargeAmount(0);
			memberRechargeRecordDbo.setShortage(memberGrade.getVip1());//减去你充值的金额  就是你还差多少金额
			memberRechargeRecordDbo.setProgressBar(new BigDecimal("0.00").setScale(2,BigDecimal.ROUND_HALF_UP));//经验条，保留两位小数
			return memberRechargeRecordDbo;
		}else {//不为空  表示之前冲过钱
			if(memberRechargeRecordDbo.getRechargeAmount() < memberGrade.getVip1()) {//小于vip1的金额 为vip0
				memberRechargeRecordDbo.setMemberGrade("vip0");
				memberRechargeRecordDbo.setShortage(memberGrade.getVip1()-memberRechargeRecordDbo.getRechargeAmount());//减去你充值的金额
				memberRechargeRecordDbo.setProgressBar(new BigDecimal
						(memberRechargeRecordDbo.getRechargeAmount()/memberGrade.getVip1()).setScale(2,BigDecimal.ROUND_HALF_UP));//经验条，保留两位小数
				memberRechargeRecordDboDao.saveMemberRechargeAmount(memberRechargeRecordDbo);
			}else if(memberRechargeRecordDbo.getRechargeAmount() >= memberGrade.getVip1() && memberRechargeRecordDbo.getRechargeAmount() < memberGrade.getVip2()) {
				memberRechargeRecordDbo.setMemberGrade("vip1");
				memberRechargeRecordDbo.setShortage(memberGrade.getVip2()-memberRechargeRecordDbo.getRechargeAmount());//减去你充值的金额
				memberRechargeRecordDbo.setProgressBar(new BigDecimal
						(memberRechargeRecordDbo.getRechargeAmount()/memberGrade.getVip2()).setScale(2,BigDecimal.ROUND_HALF_UP));//经验条，保留两位小数
				memberRechargeRecordDboDao.saveMemberRechargeAmount(memberRechargeRecordDbo);
			}else if(memberRechargeRecordDbo.getRechargeAmount() >= memberGrade.getVip2() && memberRechargeRecordDbo.getRechargeAmount() < memberGrade.getVip3()) {
				memberRechargeRecordDbo.setMemberGrade("vip2");
				memberRechargeRecordDbo.setShortage(memberGrade.getVip3()-memberRechargeRecordDbo.getRechargeAmount());//减去你充值的金额
				memberRechargeRecordDbo.setProgressBar(new BigDecimal
						(memberRechargeRecordDbo.getRechargeAmount()/memberGrade.getVip3()).setScale(2,BigDecimal.ROUND_HALF_UP));//经验条，保留两位小数
				memberRechargeRecordDboDao.saveMemberRechargeAmount(memberRechargeRecordDbo);
			}else if(memberRechargeRecordDbo.getRechargeAmount() >= memberGrade.getVip3() && memberRechargeRecordDbo.getRechargeAmount() < memberGrade.getVip4()) {
				memberRechargeRecordDbo.setMemberGrade("vip3");
				memberRechargeRecordDbo.setShortage(memberGrade.getVip4()-memberRechargeRecordDbo.getRechargeAmount());//减去你充值的金额
				memberRechargeRecordDbo.setProgressBar(new BigDecimal
						(memberRechargeRecordDbo.getRechargeAmount()/memberGrade.getVip4()).setScale(2,BigDecimal.ROUND_HALF_UP));//经验条，保留两位小数
				memberRechargeRecordDboDao.saveMemberRechargeAmount(memberRechargeRecordDbo);
			}else if(memberRechargeRecordDbo.getRechargeAmount() >= memberGrade.getVip4() && memberRechargeRecordDbo.getRechargeAmount() < memberGrade.getVip5()) {
				memberRechargeRecordDbo.setMemberGrade("vip4");
				memberRechargeRecordDbo.setShortage(memberGrade.getVip5()-memberRechargeRecordDbo.getRechargeAmount());//减去你充值的金额
				memberRechargeRecordDbo.setProgressBar(new BigDecimal
						(memberRechargeRecordDbo.getRechargeAmount()/memberGrade.getVip5()).setScale(2,BigDecimal.ROUND_HALF_UP));//经验条，保留两位小数
				memberRechargeRecordDboDao.saveMemberRechargeAmount(memberRechargeRecordDbo);
			}else if(memberRechargeRecordDbo.getRechargeAmount() >= memberGrade.getVip5()) {
				memberRechargeRecordDbo.setMemberGrade("vip5");
				memberRechargeRecordDbo.setShortage(0);//已经满级   不差钱
				memberRechargeRecordDbo.setProgressBar(new BigDecimal(1).setScale(2,BigDecimal.ROUND_HALF_UP));//经验条，保留两位小数
				memberRechargeRecordDboDao.saveMemberRechargeAmount(memberRechargeRecordDbo);
			}
			return memberRechargeRecordDbo;
		}
		
	}
	
	
	/**更新会员等级定时器
	 * 
	@Scheduled(cron = "0/5 * * * * ?")//每5秒走一次
	public void sharetimer() {

	}
	**/
	
}
