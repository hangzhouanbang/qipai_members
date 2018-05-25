package com.anbang.qipai.members.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.web.vo.CommonVO;

@RestController
@RequestMapping("/auth")
public class MemberAuthController {

	@Autowired
	private MemberAuthService memberAuthService;

	@RequestMapping(value = "/trytoken")
	@ResponseBody
	public CommonVO trytoken(String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId != null) {
			Map data = new HashMap();
			data.put("memberId", memberId);
			vo.setData(data);
		} else {
			vo.setSuccess(false);
		}
		return vo;
	}

}
