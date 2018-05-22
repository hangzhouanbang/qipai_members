package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.q.dao.AuthorizationDboDao;
import com.anbang.qipai.members.cqrs.q.dao.mongodb.repository.AuthorizationDboRepository;
import com.anbang.qipai.members.cqrs.q.dbo.AuthorizationDbo;

@Component
public class MongodbAuthorizationDboDao implements AuthorizationDboDao {

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
