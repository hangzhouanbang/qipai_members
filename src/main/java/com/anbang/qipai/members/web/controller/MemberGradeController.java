package com.anbang.qipai.members.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.msg.service.MemberGradeMsgService;
import com.anbang.qipai.members.plan.domain.MemberGrade;
import com.anbang.qipai.members.plan.service.MemberGradeService;
import com.anbang.qipai.members.web.vo.CommonVO;

import net.sf.json.JSONObject;

/**会员等级controller
 * @author 程佳 2018.6.21
 * **/
@RestController
@RequestMapping("/grade")
public class MemberGradeController {
	
	@Autowired
	private MemberGradeService memberGradeQueryService;
	
	@Autowired
	private MemberGradeMsgService memberGradeMsgService;

	@RequestMapping("/insert_grade")
	@ResponseBody
	public CommonVO insert_grade(@RequestBody MemberGrade memberGrade) {
		CommonVO co = new CommonVO();
//		JSONObject jsonobj =  JSONObject.fromObject(json);
//		MemberGradeDbo memberGradeDbo = (MemberGradeDbo) JSONObject.toBean(jsonobj);
		memberGradeQueryService.insert_grade(memberGrade);
		MemberGrade memberGrades = memberGradeQueryService.find_grade();
		memberGradeMsgService.insert_grade(memberGrades);
		return co;
	}
}
