package com.anbang.qipai.members.plan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.dao.CreateMemberConfigurationDao;
import com.anbang.qipai.members.plan.domain.CreateMemberConfiguration;

@Component
public class ConfigurationService {

	@Autowired
	private CreateMemberConfigurationDao createMemberConfigurationDao;

	public void saveCreateMemberConfiguration(int goldForNewMember) {
		CreateMemberConfiguration config = new CreateMemberConfiguration();
		config.setId("1");
		config.setGoldForNewMember(goldForNewMember);
		createMemberConfigurationDao.save(config);
	}

	public CreateMemberConfiguration findCreateMemberConfiguration() {
		return createMemberConfigurationDao.find();
	}

}
