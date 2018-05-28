package com.anbang.qipai.members.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.plan.service.ConfigurationService;
import com.anbang.qipai.members.web.vo.CommonVO;

@RestController
@RequestMapping("/conf")
public class ConfigurationController {

	@Autowired
	private ConfigurationService configurationService;

	@RequestMapping(value = "/createmember")
	@ResponseBody
	public CommonVO createmember(int goldForNewMember) {
		configurationService.saveCreateMemberConfiguration(goldForNewMember);
		return new CommonVO();
	}

}
