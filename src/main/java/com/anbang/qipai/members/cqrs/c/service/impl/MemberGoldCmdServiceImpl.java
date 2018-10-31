package com.anbang.qipai.members.cqrs.c.service.impl;

import com.anbang.qipai.members.cqrs.c.domain.prize.ObatinSigningPrizeRecord;
import com.anbang.qipai.members.cqrs.c.domain.prize.PrizeEnum;
import com.anbang.qipai.members.cqrs.c.domain.prize.SignPrizeManager;
import com.anbang.qipai.members.cqrs.c.domain.sign.*;
import com.anbang.qipai.members.cqrs.c.domain.vip.VIPEnum;
import com.anbang.qipai.members.cqrs.c.domain.vip.VipSignGiftScoreManager;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.domain.gold.MemberGoldAccountManager;
import com.anbang.qipai.members.cqrs.c.service.MemberGoldCmdService;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.dml.accounting.TextAccountingSummary;

@Component
public class MemberGoldCmdServiceImpl extends CmdServiceBase implements MemberGoldCmdService {

	@Override
	public AccountingRecord giveGoldToMember(String memberId, Integer amount, String textSummary, Long currentTime)
			throws MemberNotFoundException {
		MemberGoldAccountManager memberGoldAccountManager = singletonEntityRepository
				.getEntity(MemberGoldAccountManager.class);
		return memberGoldAccountManager.giveGoldToMember(memberId, amount, new TextAccountingSummary(textSummary),
				currentTime);
	}

	@Override
	public AccountingRecord withdraw(String memberId, Integer amount, String textSummary, Long currentTime)
			throws InsufficientBalanceException, MemberNotFoundException {
		MemberGoldAccountManager memberGoldAccountManager = singletonEntityRepository
				.getEntity(MemberGoldAccountManager.class);
		AccountingRecord rcd = memberGoldAccountManager.withdraw(memberId, amount,
				new TextAccountingSummary(textSummary), currentTime);
		return rcd;
	}

	@Override
	public ObatinSigningPrizeRecord obtainSignPrizeGold(String memberId, SignTypeEnum signtype) throws OpportunityInvalidUsedException, OpportunityNotExistsExcetion,
            MemberNotFoundException {
        SignPrizeManager signPrizeManager=this.singletonEntityRepository.getEntity(SignPrizeManager.class);
        final PrizeEnum prize=signPrizeManager.getPrize(signtype);
        if (!PrizeEnum.isGoldType(prize)){
            throw new IllegalArgumentException("prize type must be gold");
        }
        SigningPrizeOpportunityManager signingPrizeOpportunityManager = this.singletonEntityRepository.getEntity(SigningPrizeOpportunityManager.class);
        ObtainSignPrizeOpportunity opportunity = signingPrizeOpportunityManager.useSignPrizeOpportunity(memberId, signtype);
        VipSignGiftScoreManager vipSignGiftScoreManager = this.singletonEntityRepository.getEntity(VipSignGiftScoreManager.class);
        final VIPEnum vipLevel=opportunity.getVipLevel();
        final int score=vipSignGiftScoreManager.getVipGiftScore(vipLevel).getScore();
        return new ObatinSigningPrizeRecord(memberId,prize,System.currentTimeMillis(),vipLevel.getLevel(),score);
	}

}
