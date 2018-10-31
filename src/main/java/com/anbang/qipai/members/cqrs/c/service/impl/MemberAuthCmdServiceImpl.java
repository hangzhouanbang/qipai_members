package com.anbang.qipai.members.cqrs.c.service.impl;

import com.anbang.qipai.members.cqrs.c.domain.CreateMemberResult;
import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.domain.gold.MemberGoldAccountManager;
import com.anbang.qipai.members.cqrs.c.domain.gold.MemberHasGoldAccountAlreadyException;
import com.anbang.qipai.members.cqrs.c.domain.hongbao.MemberHongBaoAccountAlreadException;
import com.anbang.qipai.members.cqrs.c.domain.hongbao.MemberHongBaoAccountManager;
import com.anbang.qipai.members.cqrs.c.domain.member.MemberIdManager;
import com.anbang.qipai.members.cqrs.c.domain.phonefee.MemberPhoneFeeAccountExistsException;
import com.anbang.qipai.members.cqrs.c.domain.phonefee.MemberPhoneFeeAccountManager;
import com.anbang.qipai.members.cqrs.c.domain.score.MemberHasScoreAccountAlreadyException;
import com.anbang.qipai.members.cqrs.c.domain.score.MemberScoreAccountManager;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthCmdService;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.TextAccountingSummary;
import com.dml.users.AuthorizationAlreadyExistsException;
import com.dml.users.ThirdAuthorization;
import com.dml.users.UserNotFoundException;
import com.dml.users.UsersManager;
import org.springframework.stereotype.Component;

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
			Integer scoreForNewMember, Long currentTime) throws AuthorizationAlreadyExistsException {
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
			String goldAccountId = memberGoldAccountManager.createGoldAccountForNewMember(memberId);
			AccountingRecord goldAr = memberGoldAccountManager.giveGoldToMember(memberId, goldForNewMember,
					new TextAccountingSummary("new member"), currentTime);

			MemberScoreAccountManager memberScoreAccountManager = singletonEntityRepository
					.getEntity(MemberScoreAccountManager.class);
			String scoreAccountId = memberScoreAccountManager.createScoreAccountForNewMember(memberId);
			AccountingRecord scoreAr = memberScoreAccountManager.giveScoreToMember(memberId, scoreForNewMember,
					new TextAccountingSummary("new member"), currentTime);

			//添加用户红包,话费账户
//            MemberGiftScoreAccountManager memberGiftScoreAccountManager = this.singletonEntityRepository.getEntity(MemberGiftScoreAccountManager.class);
//            String giftScoreAccountId =  memberGiftScoreAccountManager.createGiftScoreAccountForNewMember(memberId);
//            AccountingRecord accountingRecordForGiftScore=memberGiftScoreAccountManager.giveGiftScoreToMember(memberId,0,new TextAccountingSummary("用户注册赠送礼券点 0"),System.currentTimeMillis());

            MemberHongBaoAccountManager memberHongBaoAccountManager=this.singletonEntityRepository.getEntity(MemberHongBaoAccountManager.class);
            String hongBaoAccountId = memberHongBaoAccountManager.createHongBaoAccountForNewMember(memberId);
            AccountingRecord accountingRecordForHongBao=memberHongBaoAccountManager.giveHongBaoToMember(memberId,0,new TextAccountingSummary("用户注册赠送红包点 0"),System.currentTimeMillis());


            MemberPhoneFeeAccountManager memberPhoneFeeAccountManager=this.singletonEntityRepository.getEntity(MemberPhoneFeeAccountManager.class);
            String phoneFeeAccountId = memberPhoneFeeAccountManager.createPhoneFeeAccountForNewMember(memberId);
            AccountingRecord accountingRecordForPhoneFee=memberPhoneFeeAccountManager.givePhoneFeeToMember(memberId,0,new TextAccountingSummary("用户注册赠送话费点 0"),System.currentTimeMillis());


            return new CreateMemberResult(memberId,goldAccountId,scoreAccountId,hongBaoAccountId,phoneFeeAccountId,goldAr,scoreAr,accountingRecordForHongBao,accountingRecordForPhoneFee);
		} catch (AuthorizationAlreadyExistsException e) {
			memberIdManager.removeMemberId(memberId);
			throw e;
		} catch (MemberHasGoldAccountAlreadyException e) {
            return new CreateMemberResult(null,null,null,null,null,null,null,null,null);
		} catch (MemberNotFoundException e) {
            return new CreateMemberResult(null,null,null,null,null,null,null,null,null);
		} catch (MemberHasScoreAccountAlreadyException e) {
            return new CreateMemberResult(null,null,null,null,null,null,null,null,null);
		} catch (MemberPhoneFeeAccountExistsException e) {
            return new CreateMemberResult(null,null,null,null,null,null,null,null,null);
        } catch (MemberHongBaoAccountAlreadException e) {
            return new CreateMemberResult(null,null,null,null,null,null,null,null,null);
        }
    }

}
