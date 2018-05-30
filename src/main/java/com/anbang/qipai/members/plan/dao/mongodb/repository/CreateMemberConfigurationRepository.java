package com.anbang.qipai.members.plan.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.members.plan.domain.CreateMemberConfiguration;

public interface CreateMemberConfigurationRepository extends MongoRepository<CreateMemberConfiguration, String> {

}
