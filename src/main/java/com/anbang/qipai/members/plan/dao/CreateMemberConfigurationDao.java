package com.anbang.qipai.members.plan.dao;

import com.anbang.qipai.members.plan.domain.CreateMemberConfiguration;

public interface CreateMemberConfigurationDao {

	void save(CreateMemberConfiguration config);

	CreateMemberConfiguration find();

}
