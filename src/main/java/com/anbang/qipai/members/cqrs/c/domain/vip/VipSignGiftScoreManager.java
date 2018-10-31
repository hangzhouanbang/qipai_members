package com.anbang.qipai.members.cqrs.c.domain.vip;

import com.anbang.qipai.members.cqrs.c.domain.prize.VipPrizeEnum;

import java.util.HashMap;
import java.util.Map;

import static com.anbang.qipai.members.cqrs.c.domain.prize.VipPrizeEnum.*;
import static com.anbang.qipai.members.cqrs.c.domain.vip.VIPEnum.*;

public class VipSignGiftScoreManager {

    private Map<VIPEnum, VipPrizeEnum> map = new HashMap<>();

    public VipSignGiftScoreManager() {
        map.put(VIP1, GIFT_SCORE_10);
        map.put(VIP2, GIFT_SCORE_20);
        map.put(VIP3, GIFT_SCORE_30);
        map.put(VIP4, GIFT_SCORE_40);
        map.put(VIP5, GIFT_SCORE_50);
    }

    public VipPrizeEnum getVipGiftScore(VIPEnum vipLevel) {
        return map.get(vipLevel);
    }

}
