package com.anbang.qipai.members.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.plan.bean.MemberGrade;
import com.anbang.qipai.members.plan.dao.MemberGradeDao;

@Service
public class MemberGradeService {
	
	@Autowired
	private MemberGradeDao memberGradeDao;
	
	/**添加会员各个等级信息
	 * **/
	public void insert_grade(MemberGrade memberGrade) {
		if(memberGrade.getVip1() != 0 && memberGrade.getVip2() != 0 && memberGrade.getVip3() != 0 && memberGrade.getVip4() != 0 && memberGrade.getVip5() != 0) {
			memberGrade.setId("1");
			memberGradeDao.insert_grade(memberGrade);
		}
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
