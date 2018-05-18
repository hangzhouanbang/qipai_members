package com.anbang.qipai.members.cqrs.q.dao;

import com.anbang.qipai.members.cqrs.q.dbo.AuthorizationDbo;

public interface AuthorizationDboDao {

	AuthorizationDbo find(boolean thirdAuth, String publisher, String uuid);

	void save(AuthorizationDbo authDbo);

}
