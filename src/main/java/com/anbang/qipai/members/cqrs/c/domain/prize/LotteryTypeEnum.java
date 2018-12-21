package com.anbang.qipai.members.cqrs.c.domain.prize;

public enum LotteryTypeEnum {
	/**
	 * 实物
	 */
	ENTIRY,
	/**
	 * 红包
	 */
	HONGBAO,
	/**
	 * 会员卡日卡
	 */
	MEMBER_CARD_DAY,
	/**
	 * 周卡会员卡
	 */
	MEMBER_CARD_WEAK,
	/**
	 * 月卡会员卡
	 */
	MEMBER_CARD_MONTH,
	/**
	 * 季卡会员卡
	 */
	MEMBER_CARD_SEASON,
	/**
	 * 话费
	 */
	PHONE_FEE,
	/**
	 * 玉石
	 */
	GOLD,

	NONE;
	;

	public static boolean isMemberCard(LotteryTypeEnum type) {
		return type == MEMBER_CARD_DAY || type == MEMBER_CARD_WEAK || type == MEMBER_CARD_MONTH
				|| type == MEMBER_CARD_SEASON;
	}

	public static LotteryTypeEnum byName(String s) {
		return LotteryTypeEnum.valueOf(s);
	}
}
