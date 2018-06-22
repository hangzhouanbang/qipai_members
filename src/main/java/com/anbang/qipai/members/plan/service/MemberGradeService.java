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
	
	
	
	
	/**更新会员等级定时器
	 * 
	@Scheduled(cron = "0/5 * * * * ?")//每5秒走一次
	public void sharetimer() {

	}
	**/
	
}
