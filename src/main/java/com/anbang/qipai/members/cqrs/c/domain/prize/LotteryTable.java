package com.anbang.qipai.members.cqrs.c.domain.prize;

import java.util.*;

import static com.anbang.qipai.members.cqrs.c.domain.sign.Constant.TOTAL_PROP_COUNT;

public class LotteryTable {

    private List<Lottery> lotterySet = new ArrayList<>();

    private Random random = new Random();

    private TreeMap<Integer, Lottery> firstLotteryTable = new TreeMap<>();

    private TreeMap<Integer, Lottery> lotteryTable = new TreeMap<>();

    private TreeMap<Integer, Lottery> firstOverstepLotteryTable = new TreeMap<>();

    private TreeMap<Integer, Lottery> overstepLotteryTable = new TreeMap<>();


    private void check(List<Lottery> updateSet) {
        int firstPropCount = 0;
        int propCount = 0;
        for (Lottery lottery : updateSet) {
            System.out.println("prop:" + lottery.getProp() + ",firstProp:" + lottery.getFirstProp());
            propCount = propCount + lottery.getProp();
            firstPropCount = firstPropCount + lottery.getFirstProp();
        }
        if (!equalsToTenMillion(firstPropCount) || !equalsToTenMillion(propCount)) {
            throw new RuntimeException("first prop count or prop count does not equal to " + TOTAL_PROP_COUNT);
        }
    }

    private boolean equalsToTenMillion(int n) {
        return n == TOTAL_PROP_COUNT;
    }

    public void updateLotterySet(List<Lottery> lotterySet) {
        this.check(lotterySet);
        this.clearBefore();
        this.lotterySet = lotterySet;
        int firstProp = 0;
        int prop = 0;
        int firstOverstepProp = 0;
        int overstepProp = 0;

        for (Lottery lottery : lotterySet) {
            prop += lottery.getProp();
            if (lottery.getProp() != 0) {
                lotteryTable.put(prop, lottery);
            }
            firstProp += lottery.getFirstProp();
            if (lottery.getFirstProp() != 0) {
                firstLotteryTable.put(firstProp, lottery);
            }
            if (lottery.isOverStep()) {
                firstOverstepProp += lottery.getFirstProp();
                overstepProp += lottery.getProp();
                firstOverstepLotteryTable.put(firstOverstepProp, lottery);
                overstepLotteryTable.put(overstepProp, lottery);
            }

        }
    }

    private void clearBefore() {
        this.lotterySet.clear();
        this.lotteryTable.clear();
        this.firstLotteryTable.clear();
    }

    public List<Lottery> list() {
        return Collections.unmodifiableList(this.lotterySet);
    }

    public RaffleHistory raffle(String memberId, boolean first) throws StockInsufficientException {
        int rand = random.nextInt(TOTAL_PROP_COUNT);
        Lottery lottery = null;
        RaffleHistory raffleHistory = null;
        if (first) {
            lottery = this.raffleConsiderofInsufficienStock(rand, firstLotteryTable, firstOverstepLotteryTable);
            raffleHistory = new RaffleHistory(memberId, lottery, System.currentTimeMillis(), true);
        } else {
            lottery = this.raffleConsiderofInsufficienStock(rand, lotteryTable, overstepLotteryTable);
            raffleHistory = new RaffleHistory(memberId, lottery, System.currentTimeMillis(), false);
        }
        return raffleHistory;
    }

    private Lottery raffleInternal(int random, TreeMap<Integer, Lottery> table) throws StockInsufficientException {
        final SortedMap<Integer, Lottery> sortedMap = table.tailMap(random, true);
        Lottery lottery = null;
        if (sortedMap.isEmpty()) {
            lottery = table.firstEntry().getValue();
        } else {
            final Integer targetKey = sortedMap.firstKey();
            lottery = sortedMap.get(targetKey);
        }
        lottery.descStock();
        return lottery;
    }

    private Lottery raffleConsiderofInsufficienStock(int random, TreeMap<Integer, Lottery> table,
                                                     TreeMap<Integer, Lottery> overstepLotteryTable) throws StockInsufficientException {
        try {
            return this.raffleInternal(random, table);
        } catch (StockInsufficientException e) {
            return this.raffleInternal(random, overstepLotteryTable);
        }
    }


}
