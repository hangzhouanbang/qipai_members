package com.anbang.qipai.members.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.cqrs.q.dbo.MemberRights;
import com.anbang.qipai.members.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.members.msg.service.MemberRightsConfigurationMsgService;
import com.anbang.qipai.members.plan.bean.MemberRightsConfiguration;
import com.anbang.qipai.members.plan.service.MemberRightsConfigurationService;
import com.anbang.qipai.members.web.vo.CommonVO;

/**
 * 会员权益controller
 * 
 * @author 程佳 2018.6.12
 **/
@RestController
@RequestMapping("/conf")
public class MemberRightsConfigurationController {
	@Autowired
	private MemberRightsConfigurationMsgService memberRightsConfigurationMsgService;

	@Autowired
	private MemberRightsConfigurationService memberRightsConfigurationService;

	@Autowired
	private MemberAuthQueryService memberAuthQueryService;

	/**
	 * 普通会员权益
	 * 
	 * @param commonuser
	 *            普通会员权益json对象
	 **/
	@RequestMapping(value = "/commonuser")
	@ResponseBody
	public CommonVO commonuser(int signGoldNumber, int goldForNewNember, int inviteIntegralNumber,
			float planGrowIntegralSpeed) {
		memberRightsConfigurationService.savePlanMemberRightsConfiguration(signGoldNumber, goldForNewNember,
				inviteIntegralNumber, planGrowIntegralSpeed);
		MemberRightsConfiguration conf = memberRightsConfigurationService.findMemberRightsConfiguration();

		MemberRights memberRights = conf.generateRightsForPlanMembers();
		memberAuthQueryService.updatePlanMembersRights(memberRights);

		// 发送消息给管理系统
		memberRightsConfigurationMsgService.saveConfiguration(conf);
		return new CommonVO();
	}

	/**
	 * vip会员权益
	 * 
	 * @param vipuser
	 *            vip会员权益json对象
	 **/
	@RequestMapping("/vipuser")
	@ResponseBody
	public CommonVO vipuser(int signGoldNumber, int inviteIntegralNumber, float vipGrowIntegralSpeed,
			float vipGrowGradeSpeed) {
		memberRightsConfigurationService.saveVipMemberRightsConfiguration(signGoldNumber, inviteIntegralNumber,
				vipGrowIntegralSpeed, vipGrowGradeSpeed);
		MemberRightsConfiguration conf = memberRightsConfigurationService.findMemberRightsConfiguration();

		MemberRights memberRights = conf.generateRightsForVipMembers();
		memberAuthQueryService.updateVipMembersRights(memberRights);

		// 发送消息给管理系统
		memberRightsConfigurationMsgService.saveConfiguration(conf);
		return new CommonVO();

	}
}
