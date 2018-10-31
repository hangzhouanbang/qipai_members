package com.anbang.qipai.members.cqrs.c.domain.sign;

import java.util.HashMap;
import java.util.Map;

public class SignCountManager {

    private final Map<String, SignCount> memberSignCountMap = new HashMap<>();

    private static final int DAY_MS = 60 * 60 * 1000 * 24;

    private class SignCount {
        private int count;
        private long lastSignTime;

        public void updateNewSign(long signTime) {
            ++count;
            this.lastSignTime = signTime;
        }

        public void expireBefore(long signTime) {
            this.count = 1;
            this.lastSignTime = signTime;
        }

    }

    private SignCount getOrCreateSignCount(String memberId) {
        SignCount signCount = this.memberSignCountMap.get(memberId);
        if (signCount == null) {
            signCount = new SignCount();
            this.memberSignCountMap.put(memberId, signCount);
        }
        return signCount;
    }

    public boolean isSignedToday(String memberId) {
        final SignCount signCount = this.getOrCreateSignCount(memberId);
        final long lastSignAsDay = signCount.lastSignTime / DAY_MS;
        final long signCurrentTime = System.currentTimeMillis();
        final long currentSignAsDay = signCurrentTime / DAY_MS;
        return lastSignAsDay == currentSignAsDay;
    }

    public long signCurrentTime(String memberId) {
        final SignCount signCount = this.getOrCreateSignCount(memberId);
        final long lastSignAsDay = signCount.lastSignTime / DAY_MS;
        final long signCurrentTime = System.currentTimeMillis();
        final long currentSignAsDay = signCurrentTime / DAY_MS;
        signCount.updateNewSign(signCurrentTime);
        if (lastSignAsDay + 1 == currentSignAsDay) {
            signCount.updateNewSign(signCurrentTime);
        } else if (lastSignAsDay == currentSignAsDay) {
            //今天已经签到过 do nothing
        } else {
            signCount.expireBefore(signCurrentTime);
        }
        return signCount.lastSignTime;
    }

    public int continuousSignDays(String memberId) {
        final SignCount signCount = this.getOrCreateSignCount(memberId);
        return signCount.count;
    }


}
