package com.anbang.qipai.members.plan.dao;

import com.anbang.qipai.members.plan.bean.MemberGrade;

public interface MemberGradeDao {

	void insert_grade(MemberGrade memberGrade);
	
	MemberGrade find_grade(String id);
}
