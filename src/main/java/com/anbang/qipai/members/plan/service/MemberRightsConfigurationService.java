package com.anbang.qipai.members.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.bean.MemberRightsConfiguration;
import com.anbang.qipai.members.plan.dao.MemberRightsConfigurationDao;

@Component
public class MemberRightsConfigurationService {
	@Autowired
	private MemberRightsConfigurationDao memberRightsConfigurationDao;

	public void savePlanMemberRightsConfiguration(int signGoldNumber, int goldForNewNember, int inviteIntegralNumber,
			int goldForAgentInvite, float planGrowIntegralSpeed) {
		MemberRightsConfiguration memberRightsConfiguration = memberRightsConfigurationDao.find();
		if (memberRightsConfiguration == null) {
			MemberRightsConfiguration conf = new MemberRightsConfiguration();
			conf.setId("1");
			conf.setSignGoldNumber(signGoldNumber);
			conf.setGoldForNewNember(goldForNewNember);
			conf.setInviteIntegralNumber(inviteIntegralNumber);
			conf.setGoldForAgentInvite(goldForAgentInvite);
			conf.setPlanGrowIntegralSpeed(planGrowIntegralSpeed);
			memberRightsConfigurationDao.save(conf);
		} else {
			memberRightsConfigurationDao.setPlanMembersRights(planGrowIntegralSpeed, goldForAgentInvite,
					goldForNewNember);
		}
		memberRightsConfigurationDao.updatePlanMembersRights(signGoldNumber, goldForNewNember, inviteIntegralNumber,
				goldForAgentInvite, planGrowIntegralSpeed);

	}

	public MemberRightsConfiguration findMemberRightsConfiguration() {
		return memberRightsConfigurationDao.find();
	}

	public void saveVipMemberRightsConfiguration(int signGoldNumber, int inviteIntegralNumber,
			float vipGrowIntegralSpeed, float vipGrowGradeSpeed) {
		MemberRightsConfiguration memberRightsConfiguration = memberRightsConfigurationDao.find();
		if (memberRightsConfiguration == null) {
			MemberRightsConfiguration conf = new MemberRightsConfiguration();
			conf.setId("1");
			conf.setSignGoldNumber(signGoldNumber);
			conf.setInviteIntegralNumber(inviteIntegralNumber);
			conf.setVipGrowIntegralSpeed(vipGrowIntegralSpeed);
			conf.setVipGrowGradeSpeed(vipGrowGradeSpeed);
			memberRightsConfigurationDao.save(conf);
		} else {
			memberRightsConfigurationDao.setVipMembersRights(vipGrowGradeSpeed);
		}
		memberRightsConfigurationDao.updateVipMembersRights(signGoldNumber, inviteIntegralNumber, vipGrowIntegralSpeed,
				vipGrowGradeSpeed);
	}

}
