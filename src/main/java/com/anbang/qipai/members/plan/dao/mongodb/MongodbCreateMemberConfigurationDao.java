package com.anbang.qipai.members.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.dao.CreateMemberConfigurationDao;
import com.anbang.qipai.members.plan.dao.mongodb.repository.CreateMemberConfigurationRepository;
import com.anbang.qipai.members.plan.domain.CreateMemberConfiguration;

@Component
public class MongodbCreateMemberConfigurationDao implements CreateMemberConfigurationDao {

	@Autowired
	private CreateMemberConfigurationRepository repository;

	@Override
	public void save(CreateMemberConfiguration config) {
		repository.save(config);
	}

	@Override
	public CreateMemberConfiguration find() {
		return repository.findOne("1");
	}

}
