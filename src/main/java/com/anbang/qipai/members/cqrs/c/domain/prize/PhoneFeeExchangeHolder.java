package com.anbang.qipai.members.cqrs.c.domain.prize;

import java.util.HashMap;
import java.util.Map;

public class PhoneFeeExchangeHolder {
    private Map<Integer/*话费点*/, Integer/*话费*/> holder = new HashMap();

    public PhoneFeeExchangeHolder() {
        holder.put(10, 10);
        holder.put(20, 20);
        holder.put(50, 50);
        holder.put(100, 100);
    }

    public Integer get(Integer score) throws ExchangeException {
        Integer hb = this.holder.get(score);
        if (hb == null) {
            throw new ExchangeException();
        }
        return hb;
    }

}
