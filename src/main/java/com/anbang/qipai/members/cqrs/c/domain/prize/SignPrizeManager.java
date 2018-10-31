package com.anbang.qipai.members.cqrs.c.domain.prize;

import com.anbang.qipai.members.cqrs.c.domain.sign.SignTypeEnum;

import java.util.HashMap;
import java.util.Map;

import static com.anbang.qipai.members.cqrs.c.domain.prize.PrizeEnum.*;
import static com.anbang.qipai.members.cqrs.c.domain.sign.SignTypeEnum.*;


public class SignPrizeManager {

    private Map<SignTypeEnum, PrizeEnum> typePrizeMap = new HashMap<>();

    public SignPrizeManager() {
        this.typePrizeMap.put(THREE, ONE_DAY_MEMBER_CARD);
        this.typePrizeMap.put(FIVE, GOLD_500);
        this.typePrizeMap.put(SEVEN, TWO_DAY_MEMBER_CARD);
        this.typePrizeMap.put(FIFTEEN, GOLD_1000);
        this.typePrizeMap.put(TWENTY, GOLD_2000);
    }

    public PrizeEnum getPrize(SignTypeEnum signPrizeType) {
        return this.typePrizeMap.get(signPrizeType);
    }

}
