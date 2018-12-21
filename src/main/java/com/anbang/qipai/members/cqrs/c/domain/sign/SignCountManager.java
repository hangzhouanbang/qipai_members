package com.anbang.qipai.members.cqrs.c.domain.sign;

import java.text.SimpleDateFormat;
import java.util.Date;
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


        public void updateNewMonth(long signTime) {
            count = 1;
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

    //当日签到
    public long signCurrentTime(String memberId) {
        final SignCount signCount = this.getOrCreateSignCount(memberId);
        final long signCurrentTime = System.currentTimeMillis();

        //标准化获取KEY
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        //上一次的签到月份
        String lastSignAsDay = simpleDateFormat.format(new Date(signCount.lastSignTime));

        //本次签到月份
        String currentSignAsDay = simpleDateFormat.format(new Date(signCurrentTime));

//        signCount.updateNewSign(signCurrentTime);
        //如果是当月签到
        if (lastSignAsDay.equals(currentSignAsDay)) {
            signCount.updateNewSign(signCurrentTime);
        } else if (!lastSignAsDay.equals(currentSignAsDay)) {
            //当月已经更新
            signCount.updateNewMonth(signCurrentTime);
        } else {
            //
//            signCount.expireBefore(signCurrentTime);
        }
        return signCount.lastSignTime;
    }

    public int continuousSignDays(String memberId) {
        final SignCount signCount = this.getOrCreateSignCount(memberId);
        return signCount.count;
    }


}
