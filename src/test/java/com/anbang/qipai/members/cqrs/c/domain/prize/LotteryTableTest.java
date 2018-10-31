package com.anbang.qipai.members.cqrs.c.domain.prize;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LotteryTableTest {

    private LotteryTable lotteryTable = new LotteryTable();

    private final int total = 1000 * 10000;

    @Before
    public void init() {
        System.out.println(0.000001 * total);
        List<Lottery> set = new ArrayList<>();
        set.add(new Lottery(String.valueOf(1), "iphone", (int) (0.000001 * total), (int) (0.00010 * total), LotteryTypeEnum.ENTIRY, 1, 2, false));
        set.add(new Lottery(String.valueOf(2), "gold * 100", (int) (0.2 * total), (int) (0.3 * total), LotteryTypeEnum.GOLD, 1, Integer.MAX_VALUE, true));
        set.add(new Lottery(String.valueOf(3), "hongbao * 100", (int) (0.4 * total), (int) (0.1 * total), LotteryTypeEnum.HONGBAO, 300, 10, false));
        set.add(new Lottery(String.valueOf(4), "hongbao * 200", (int) (0.1 * total), (int) (0.3 * total), LotteryTypeEnum.HONGBAO, 1, 10, false));
        set.add(new Lottery(String.valueOf(5), "gold * 200", (int) (0.299999 * total), (int) (0.2999 * total), LotteryTypeEnum.ENTIRY, 1, Integer.MAX_VALUE, true));
        lotteryTable.updateLotterySet(set);
    }

    @Test
    public void test() throws StockInsufficientException {
        RaffleHistory history = null;
        for (int i = 0; i < 2000; i++) {
            if (i == 0) {
                history = lotteryTable.raffle("wenruo", true);
            } else
                history = lotteryTable.raffle("wenruo", false);
            System.out.println(history);
        }

    }

}
