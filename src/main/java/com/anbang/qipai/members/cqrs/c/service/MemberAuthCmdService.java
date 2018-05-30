package com.anbang.qipai.members.cqrs.c.service;

import com.anbang.qipai.members.cqrs.c.domain.CreateMemberResult;
import com.dml.users.AuthorizationAlreadyExistsException;
import com.dml.users.UserNotFoundException;

public interface MemberAuthCmdService {

	void addThirdAuth(String publisher, String uuid, String memberId)
			throws UserNotFoundException, AuthorizationAlreadyExistsException;

	CreateMemberResult createMemberAndAddThirdAuth(String publisher, String uuid, Integer goldForNewMember,
			Long currentTime) throws AuthorizationAlreadyExistsException;

}
