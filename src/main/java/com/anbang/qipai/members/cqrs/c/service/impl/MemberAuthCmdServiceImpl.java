package com.anbang.qipai.members.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.c.domain.CreateMemberResult;
import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.domain.gold.MemberGoldAccountManager;
import com.anbang.qipai.members.cqrs.c.domain.gold.MemberHasGoldAccountAlreadyException;
import com.anbang.qipai.members.cqrs.c.domain.member.MemberIdManager;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthCmdService;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.TextAccountingSummary;
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
	public CreateMemberResult createMemberAndAddThirdAuth(String publisher, String uuid, Integer goldForNewMember,
			Long currentTime) throws AuthorizationAlreadyExistsException {
		MemberIdManager memberIdManager = singletonEntityRepository.getEntity(MemberIdManager.class);
		UsersManager usersManager = singletonEntityRepository.getEntity(UsersManager.class);
		String memberId = memberIdManager.createMemberId(currentTime);
		ThirdAuthorization auth = new ThirdAuthorization();
		auth.setPublisher(publisher);
		auth.setUuid(uuid);
		try {
			usersManager.createUserWithAuth(memberId, auth);
			MemberGoldAccountManager memberGoldAccountManager = singletonEntityRepository
					.getEntity(MemberGoldAccountManager.class);
			String accountId = memberGoldAccountManager.createGoldAccountForNewMember(memberId);
			AccountingRecord ar = memberGoldAccountManager.giveGoldToMember(memberId, goldForNewMember,
					new TextAccountingSummary("new member"));
			return new CreateMemberResult(memberId, accountId, ar);
		} catch (AuthorizationAlreadyExistsException e) {
			memberIdManager.removeMemberId(memberId);
			throw e;
		} catch (MemberHasGoldAccountAlreadyException e) {
			return new CreateMemberResult(null, null, null);
		} catch (MemberNotFoundException e) {
			return new CreateMemberResult(null, null, null);
		}
	}

}
