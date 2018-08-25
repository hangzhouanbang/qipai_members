package com.anbang.qipai.members.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.members.msg.channel.source.GradeSoure;
import com.anbang.qipai.members.plan.bean.MemberGrade;
import com.anbang.qipai.members.web.vo.CommonVO;

@EnableBinding(GradeSoure.class)
public class MemberGradeMsgService {
	

	@Autowired
	private GradeSoure gradeSoure;
	
	public void insert_grade(MemberGrade memberGrade) {
		CommonVO co = new CommonVO();
		co.setMsg("newCradeGrade");
		co.setData(memberGrade);
		gradeSoure.grade().send(MessageBuilder.withPayload(co).build());
	}
}
