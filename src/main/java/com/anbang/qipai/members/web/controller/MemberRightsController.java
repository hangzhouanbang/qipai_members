package com.anbang.qipai.members.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.msg.service.MemberShipMsgService;
import com.anbang.qipai.members.plan.domain.MemberRights;
import com.anbang.qipai.members.plan.service.MemberRightsService;
import com.anbang.qipai.members.web.vo.CommonVO;

/**会员权益controller
 * @author 程佳 2018.6.12
 * **/
@RestController
@RequestMapping("/conf")
public class MemberRightsController {
	@Autowired
	private MemberShipMsgService memberShipRightsMsgService;
	
	@Autowired
	private MemberRightsService memberShipConfigurationService;
	
	/**普通会员权益
	 * @param commonuser 普通会员权益json对象
	 * **/
	@RequestMapping(value = "/commonuser")
	@ResponseBody
	public CommonVO commonuser(int signGoldNumber,int goldForNewNember,
			int inviteIntegralNumber,float planGrowIntegralSpeed) {
		memberShipConfigurationService.savePlanShipConfiguration(signGoldNumber,goldForNewNember,inviteIntegralNumber,planGrowIntegralSpeed);
		//查询普通用户权益
		MemberRights createMemberConfiguration = memberShipConfigurationService.findMemberCreateMemberConfiguration();
		//发送消息给管理系统
		memberShipRightsMsgService.saveCommonUser(createMemberConfiguration);
		return new CommonVO();
	}
	
	/**vip会员权益
	 * @param vipuser vip会员权益json对象
	 * **/
	@RequestMapping("/vipuser")
	@ResponseBody
	public CommonVO vipuser(int signGoldNumber,int inviteIntegralNumber,float vipGrowIntegralSpeed,float vipGrowGradeSpeed) {
		memberShipConfigurationService.saveVipShipConfiguration(signGoldNumber,inviteIntegralNumber,vipGrowIntegralSpeed,vipGrowGradeSpeed);
		//查询vip用户权益
		MemberRights createMemberConfiguration = memberShipConfigurationService.findMemberCreateMemberConfiguration();
		//发送消息给管理系统
		memberShipRightsMsgService.savevipuser(createMemberConfiguration);
		return new CommonVO();
		
	}
}
