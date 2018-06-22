package com.anbang.qipai.members.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.plan.service.MemberGradeInFoService;
import com.anbang.qipai.members.web.vo.CommonVO;

/**会员充值记录controller
 * @author 程佳 2018.6.22
 * **/
@RestController
@RequestMapping("/grade_info")
public class MemberGradeInFoController {
	
	@Autowired
	private MemberGradeInFoService memberGradeInFoService;
	
	@RequestMapping("/find_grade_info")
	@ResponseBody
	public CommonVO find_recharge_record() {
		String memberId = "";//测试用
		CommonVO co = memberGradeInFoService.find_grade_info(memberId);
		return co;
	}
}
