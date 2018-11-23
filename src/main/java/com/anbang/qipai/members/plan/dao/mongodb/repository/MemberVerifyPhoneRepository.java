package com.anbang.qipai.members.plan.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.members.plan.bean.MemberVerifyPhone;

public interface MemberVerifyPhoneRepository extends MongoRepository<MemberVerifyPhone, String> {

}
