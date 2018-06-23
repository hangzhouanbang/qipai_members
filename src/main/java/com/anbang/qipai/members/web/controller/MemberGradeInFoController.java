package com.anbang.qipai.members.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.plan.service.MemberGradeInFoService;
import com.anbang.qipai.members.web.vo.GradeVo;

/**会员充值记录controller
 * @author 程佳 2018.6.22
 * **/
@RestController
@RequestMapping("/grade_info")
public class MemberGradeInFoController {
	
	@Autowired
	private MemberGradeInFoService memberGradeInFoService;
	
	@Autowired
	private MemberAuthService memberAuthService;
	
	@RequestMapping("/find_grade_info")
	@ResponseBody
	public GradeVo find_recharge_record(String token) {
		GradeVo go = new GradeVo();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if(token == null) {
			go.setSuccess(false);
			go.setMsg("invalid token");
		}
		//String memberId = "881071";//测试用
		go = memberGradeInFoService.find_grade_info(memberId);
		return go;
	}
}
