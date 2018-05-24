package com.anbang.qipai.members.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.c.domain.MemberIdManager;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthCmdService;
import com.dml.users.AuthorizationAlreadyExistsException;
import com.dml.users.ThirdAuthorization;
import com.dml.users.UserNotFoundException;
import com.dml.users.UsersManager;

@Component
public class MemberAuthCmdServiceImpl extends CmdServiceBase implements MemberAuthCmdService {

	@Override
	public void addThirdAuth(String publisher, String uuid, String memberId)
			throws UserNotFoundException, AuthorizationAlreadyExistsException {
		UsersManager usersManager = singletonEntityRepository.getEntity(UsersManager.class);
		ThirdAuthorization auth = new ThirdAuthorization();
		auth.setPublisher(publisher);
		auth.setUuid(uuid);
		usersManager.addAuthForUser(memberId, auth);
	}

	@Override
	public String createMemberAndAddThirdAuth(String publisher, String uuid, Long currentTime)
			throws AuthorizationAlreadyExistsException {
		MemberIdManager memberIdManager = singletonEntityRepository.getEntity(MemberIdManager.class);
		UsersManager usersManager = singletonEntityRepository.getEntity(UsersManager.class);
		String memberId = memberIdManager.createMemberId(currentTime);
		ThirdAuthorization auth = new ThirdAuthorization();
		auth.setPublisher(publisher);
		auth.setUuid(uuid);
		try {
			usersManager.createUserWithAuth(memberId, auth);
			return memberId;
		} catch (AuthorizationAlreadyExistsException e) {
			memberIdManager.removeMemberId(memberId);
			throw e;
		}
	}

}
