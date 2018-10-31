package com.anbang.qipai.members.cqrs.c.domain.prize;

import java.util.HashMap;
import java.util.Map;

public class HongBaoExchangeHolder {

    private Map<Integer/*红包点*/, Integer/*红包*/> holder = new HashMap<>();

    public HongBaoExchangeHolder() {
        holder.put(100, 5);
        holder.put(200, 10);
        holder.put(400, 20);
        holder.put(1000, 50);
    }

    public Integer get(Integer score) throws ExchangeException {
        Integer hb = this.holder.get(score);
        if (hb == null) {
            throw new ExchangeException();
        }
        return hb;
    }

}
