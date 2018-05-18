package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;

import com.anbang.qipai.members.cqrs.q.dao.AuthorizationDboDao;
import com.anbang.qipai.members.cqrs.q.dao.mongodb.repository.AuthorizationDboRepository;
import com.anbang.qipai.members.cqrs.q.dbo.AuthorizationDbo;
import com.highto.framework.data.mongodb.MongodbDaoBase;

public class MongodbAuthorizationDboDao extends MongodbDaoBase implements AuthorizationDboDao {

	@Autowired
	private AuthorizationDboRepository repository;

	@Override
	public AuthorizationDbo find(boolean thirdAuth, String publisher, String uuid) {
		return repository.findOneByThirdAuthAndPublisherAndUuid(thirdAuth, publisher, uuid);
	}

	@Override
	public void save(AuthorizationDbo authDbo) {
		repository.save(authDbo);
	}

}
