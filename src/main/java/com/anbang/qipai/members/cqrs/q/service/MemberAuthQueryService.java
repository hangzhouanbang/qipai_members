package com.anbang.qipai.members.cqrs.q.service;

import com.anbang.qipai.members.cqrs.c.domain.prize.LotteryTypeEnum;
import com.anbang.qipai.members.cqrs.c.domain.prize.PrizeEnum;
import com.anbang.qipai.members.cqrs.c.domain.sign.Constant;
import com.anbang.qipai.members.cqrs.q.dao.AuthorizationDboDao;
import com.anbang.qipai.members.cqrs.q.dao.MemberDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.AuthorizationDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberRights;
import com.anbang.qipai.members.plan.bean.MemberGrade;
import com.anbang.qipai.members.plan.bean.MemberOrder;
import com.anbang.qipai.members.plan.bean.MemberRightsConfiguration;
import com.anbang.qipai.members.plan.dao.MemberGradeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static com.anbang.qipai.members.cqrs.c.domain.prize.PrizeEnum.ONE_DAY_MEMBER_CARD;
import static com.anbang.qipai.members.cqrs.c.domain.prize.PrizeEnum.TWO_DAY_MEMBER_CARD;

@Component
public class MemberAuthQueryService {

	@Autowired
	private AuthorizationDboDao authorizationDboDao;

	@Autowired
	private MemberDboDao memberDboDao;

	@Autowired
	private MemberGradeDao memberGradeDao;

	public AuthorizationDbo findThirdAuthorizationDbo(String publisher, String uuid) {
		return authorizationDboDao.find(true, publisher, uuid);
	}

	public void updateMember(String memberId, String nickname, String headimgurl, Integer sex) {
		String gender = "unknow";
		if (sex.intValue() == 1) {
			gender = "male";
		}
		if (sex.intValue() == 2) {
			gender = "female";
		}
		memberDboDao.update(memberId, nickname, headimgurl, gender);
	}

	public void createMemberAndAddThirdAuth(String memberId, String publisher, String uuid,
			MemberRightsConfiguration memberRightsConfiguration) {
		MemberDbo memberDbo = new MemberDbo();
		memberDbo.setId(memberId);
		memberDbo.setCreateTime(System.currentTimeMillis());
		if (memberRightsConfiguration != null) {
			memberDbo.setRights(memberRightsConfiguration.generateRightsForPlanMembers());
		}
		memberDboDao.save(memberDbo);

		AuthorizationDbo authDbo = new AuthorizationDbo();
		authDbo.setMemberId(memberId);
		authDbo.setPublisher(publisher);
		authDbo.setThirdAuth(true);
		authDbo.setUuid(uuid);
		authorizationDboDao.save(authDbo);
	}

	public void addThirdAuth(String publisher, String uuid, String memberId) {
		AuthorizationDbo authDbo = new AuthorizationDbo();
		authDbo.setMemberId(memberId);
		authDbo.setPublisher(publisher);
		authDbo.setThirdAuth(true);
		authDbo.setUuid(uuid);
		authorizationDboDao.save(authDbo);
	}

	public MemberDbo findMemberById(String memberId) {
		return memberDboDao.findMemberById(memberId);
	}

	public void updatePlanMembersRights(MemberRights memberRights) {
		memberDboDao.updatePlanMembersRights(memberRights);
	}

	public void updateVipMembersRights(MemberRights memberRights) {
		memberDboDao.updateVipMembersRights(memberRights);
	}

	public long getAmountByVip(boolean vip) {
		return memberDboDao.getAmountByVip(vip);
	}

	public List<MemberDbo> findMemberByVip(int page, int size, boolean vip) {
		return memberDboDao.findMemberByVip(page, size, vip);
	}

	public MemberDbo registerPhone(String memberId, String phone) {
		memberDboDao.updateMemberPhone(memberId, phone);
		return memberDboDao.findMemberById(memberId);
	}

	public MemberDbo rechargeVip(String memberId, long vipTime) {
		memberDboDao.updateMemberVIP(memberId, true);
		MemberDbo member = memberDboDao.findMemberById(memberId);
		long vipEndTime = member.getVipEndTime();
		if (vipEndTime > System.currentTimeMillis()) {
			vipEndTime = vipTime + vipEndTime;
		} else {
			vipEndTime = vipTime + System.currentTimeMillis();
		}
		memberDboDao.updateMemberVipEndTime(memberId, vipEndTime);
		return memberDboDao.findMemberById(memberId);
	}

	public MemberDbo deliver(MemberOrder order) {
		MemberDbo member = memberDboDao.findMemberById(order.getReceiverId());
		if (order.getVipTime() > 0) {
			memberDboDao.updateMemberVIP(member.getId(), true);
			long vipEndTime = member.getVipEndTime();
			if (vipEndTime > System.currentTimeMillis()) {
				vipEndTime = order.getVipTime() + vipEndTime;
			} else {
				vipEndTime = order.getVipTime() + System.currentTimeMillis();
			}
			memberDboDao.updateMemberVipEndTime(member.getId(), vipEndTime);
		}
		BigDecimal b1 = new BigDecimal(Double.toString(order.getTotalamount()));
		BigDecimal b2 = new BigDecimal(Double.toString(member.getVipScore()));
		double vipScore = b1.add(b2).doubleValue();
		memberDboDao.updateMemberVipScore(member.getId(), vipScore);
		MemberGrade grade = memberGradeDao.find_grade("1");
		if (grade != null) {
			int level = member.getVipLevel();
			double score = grade.getLevel(level);
			if (vipScore >= score) {
				level += 1;
				memberDboDao.updateMemberVipLevel(member.getId(), level);
			}
		}
		BigDecimal b3 = new BigDecimal(Double.toString(order.getTotalamount()));
		BigDecimal b4 = new BigDecimal(Double.toString(member.getCost()));
		double cost = b3.add(b4).doubleValue();
		memberDboDao.updateMemberCost(member.getId(), cost);
		return memberDboDao.findMemberById(member.getId());
	}


	public MemberDbo prolongPrizeVipTime(String memberId, PrizeEnum prizeEnum){

	    if (!PrizeEnum.isMemberCardType(prizeEnum)){
	        throw new IllegalArgumentException("prize type must be member card");
        }
        long time=0;
	    if (prizeEnum == ONE_DAY_MEMBER_CARD){
	        time= Constant.ONE_DAY_MS * 1;
        }
        if (prizeEnum==TWO_DAY_MEMBER_CARD){
	        time=Constant.ONE_DAY_MS * 2;
        }
        return this.prolongVipTime(memberId,time);
    }

    public MemberDbo prolongVipTime(String memberId , long time){
		memberDboDao.updateMemberVIP(memberId, true);
        MemberDbo member = this.memberDboDao.findMemberById(memberId);
        long vipEndTime = member.getVipEndTime();
        if (vipEndTime > System.currentTimeMillis()) {
            vipEndTime = time + vipEndTime;
        } else {
            vipEndTime = time + System.currentTimeMillis();
        }
        member.setVipEndTime(vipEndTime);
        memberDboDao.updateMemberVipEndTime(memberId,vipEndTime);
        return member;
    }




	public MemberDbo updateMemberVip(String memberId, boolean vip) {
		memberDboDao.updateMemberVIP(memberId, vip);
		return memberDboDao.findMemberById(memberId);
	}

	public MemberDbo updateMemberBindAgent(String memberId, String agentId, boolean bindAgent) {
		memberDboDao.updateMemberBindAgent(memberId, agentId, bindAgent);
		return memberDboDao.findMemberById(memberId);
	}

	public MemberDbo updateMemberRealUser(String memberId, String realName, String IDcard, boolean verify) {
		memberDboDao.updateMemberRealUser(memberId, realName, IDcard, verify);
		return memberDboDao.findMemberById(memberId);
	}

    public void prolongVipTimeByRaffle(String memberId, LotteryTypeEnum lotteryType, int singleNum) {
    	long time=0;
    	switch (lotteryType){
            case MEMBER_CARD_DAY:time=Constant.ONE_DAY_MS*1*singleNum;break;
            case MEMBER_CARD_WEAK:time=Constant.ONE_DAY_MS*7*singleNum;break;
            case MEMBER_CARD_MONTH:time=Constant.ONE_DAY_MS*30*singleNum;break;
            case MEMBER_CARD_SEASON:time=Constant.ONE_DAY_MS*90*singleNum;break;
            default:throw new RuntimeException("lottery type must be membercard");
        }
        this.prolongVipTime(memberId,time);
    }
}
