package com.anbang.qipai.members.cqrs.q.dao.mongodb;


import com.anbang.qipai.members.QipaiMembersApplication;
import com.anbang.qipai.members.cqrs.c.domain.prize.Lottery;
import com.anbang.qipai.members.cqrs.c.domain.prize.LotteryTypeEnum;
import com.anbang.qipai.members.cqrs.q.dbo.LotteryDbo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QipaiMembersApplication.class)
public class MongodbLotteryDboDaoTest {

    private int total = 1000 * 10000;

    private List<LotteryDbo> lotteryDboList;

    @Autowired
    private MongodbLotteryDboDao mongodbLotteryDboDao;

    @Before
    public void init() {
        List<Lottery> list = new ArrayList<>();
        list.add(new Lottery(String.valueOf(1), "iphone", (int) (0.000001 * total), (int) (0.00010 * total), LotteryTypeEnum.ENTIRY, 1, 5, false));
        list.add(new Lottery(String.valueOf(2), "gold * 100", (int) (0.2 * total), (int) (0.3 * total), LotteryTypeEnum.GOLD, 100, Integer.MAX_VALUE, true));
        list.add(new Lottery(String.valueOf(3), "hongbao * 100", (int) (0.4 * total), (int) (0.1 * total), LotteryTypeEnum.HONGBAO, 100, 10000, false));
        list.add(new Lottery(String.valueOf(4), "phonefee * 200", (int) (0.1 * total), (int) (0.3 * total), LotteryTypeEnum.PHONE_FEE, 200, 10000, false));
        list.add(new Lottery(String.valueOf(5), "gold * 200", (int) (0.299999 * total), (int) (0.2999 * total), LotteryTypeEnum.GOLD, 200, Integer.MAX_VALUE, true));
        lotteryDboList = new ArrayList<>();
        for (Lottery lottery : list) {
            LotteryDbo lotteryDbo = new LotteryDbo();
            lotteryDbo.setName(lottery.getName());
            lotteryDbo.setFirstProp(lottery.getFirstProp());
            lotteryDbo.setProp(lottery.getProp());
            lotteryDbo.setOverStep(lottery.isOverStep());
            lotteryDbo.setSingleNum(lottery.getSingleNum());
            lotteryDbo.setStock(lottery.getStock());
            lotteryDbo.setType(lottery.getType());
            lotteryDboList.add(lotteryDbo);
        }
    }


    @Test
    public void saveAll() {
        this.mongodbLotteryDboDao.saveAll(lotteryDboList);
    }

    @Test
    public void findUndiscard() {
        lotteryDboList = this.mongodbLotteryDboDao.findUndiscard();
        for (LotteryDbo lotteryDbo : lotteryDboList) {
            System.out.println(lotteryDbo);
        }
    }


}
