package com.anbang.qipai.members.cqrs.c.service;

import com.dml.users.AuthorizationAlreadyExistsException;
import com.dml.users.UserNotFoundException;

public interface MemberAuthCmdService {

	void addThirdAuth(String publisher, String uuid, String memberId)
			throws UserNotFoundException, AuthorizationAlreadyExistsException;

	String createMemberAndAddThirdAuth(String publisher, String uuid, long currentTime)
			throws AuthorizationAlreadyExistsException;

}
