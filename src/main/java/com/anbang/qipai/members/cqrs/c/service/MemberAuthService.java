package com.anbang.qipai.members.cqrs.c.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.c.service.impl.CmdServiceBase;
import com.dml.users.AuthException;
import com.dml.users.AuthorizationNotFoundException;
import com.dml.users.PublisherUniqueStringAuthKey;
import com.dml.users.UserSessionsManager;
import com.dml.users.UsersManager;

@Component
public class MemberAuthService extends CmdServiceBase {

	private static long sessionKeepaliveTime = 30 * 24 * 60 * 60 * 1000;

	@Autowired
	private UserSessionsManager userSessionsManager;

	public String thirdAuth(String publisher, String uuid) throws AuthorizationNotFoundException, AuthException {
		UsersManager usersManager = singletonEntityRepository.getEntity(UsersManager.class);
		PublisherUniqueStringAuthKey authKey = new PublisherUniqueStringAuthKey();
		authKey.setPublisher(publisher);
		authKey.setUuid(uuid);
		String memberId = usersManager.authAndGetUserId(authKey);
		String sessionId = UUID.randomUUID().toString();
		userSessionsManager.createEngrossSessionForUser(memberId, sessionId, System.currentTimeMillis(),
				sessionKeepaliveTime);
		return sessionId;
	}

	public String getMemberIdBySessionId(String sessionId) {
		userSessionsManager.updateUserSession(sessionId, System.currentTimeMillis(), sessionKeepaliveTime);
		return userSessionsManager.getUserIdBySessionId(sessionId);
	}

}
