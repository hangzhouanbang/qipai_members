package com.anbang.qipai.members.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.msg.service.CreateMemberMsgService;
import com.anbang.qipai.members.plan.domain.CreateMemberConfiguration;
import com.anbang.qipai.members.plan.service.ConfigurationService;
import com.anbang.qipai.members.web.vo.CommonVO;

@RestController
@RequestMapping("/conf")
public class ConfigurationController {

	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private CreateMemberMsgService createMemberMsgService;
	
	@RequestMapping(value = "/createmember")
	@ResponseBody
	public CommonVO createmember(int goldForNewMember) {
		configurationService.saveCreateMemberConfiguration(goldForNewMember);
		//查询金币信息 
		CreateMemberConfiguration member = configurationService.findCreateMemberConfiguration();
		//发送消息给管理系统
		createMemberMsgService.createMember(member);
		return new CommonVO();
	}

}
