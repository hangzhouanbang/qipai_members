package com.anbang.qipai.members.cqrs.q.service;

import com.anbang.qipai.members.cqrs.c.domain.sign.Constant;
import com.anbang.qipai.members.cqrs.q.dao.CumulativeRaffleDboDao;
import com.anbang.qipai.members.cqrs.q.dao.LotteryDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.CumulativeRaffleDbo;
import com.anbang.qipai.members.cqrs.q.dbo.Lottery;
import com.anbang.qipai.members.cqrs.q.dbo.LotteryDbo;
import com.anbang.qipai.members.web.vo.CommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class LotteryQueryService {

    @Autowired
    private LotteryDboDao lotteryDboDao;

    @Autowired
    private CumulativeRaffleDboDao cumulativeRaffleDboDao;

    public List<LotteryDbo> findAll() {
        return lotteryDboDao.findUndiscard();
    }

    public List<LotteryDbo> findExtra() {
        return lotteryDboDao.findExtraLottey();
    }

    public void discardAll() {
        this.lotteryDboDao.discardAll();
    }

    public void discardBeforeAndSaveAll(List<LotteryDbo> lotteryDboList) {
        this.lotteryDboDao.discardAll();
        this.lotteryDboDao.saveAll(lotteryDboList);
    }

    public LotteryDbo findLotteryByID(String id) {
        return lotteryDboDao.findLotteryById(id);
    }

    public int findCumulateDay(String memberId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        long currentTime = System.currentTimeMillis();
        String formatTime = simpleDateFormat.format(new Date(currentTime));
        CumulativeRaffleDbo cumulativeRaffleDbo = cumulativeRaffleDboDao.findByMemberIdAndTime(memberId, formatTime);
        if (cumulativeRaffleDbo == null) {
            return 0;
        } else {
            return cumulativeRaffleDbo.getCumulativeDay();
        }
    }


    public CumulativeRaffleDbo findOneRecord(String memberId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        long currentTime = System.currentTimeMillis();
        String formatTime = simpleDateFormat.format(new Date(currentTime));
        return cumulativeRaffleDboDao.findByMemberIdAndTime(memberId, formatTime);
    }


    public LotteryDbo addCumulativeDay(String memberId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        long currentTime = System.currentTimeMillis();
        String formatTime = simpleDateFormat.format(new Date(currentTime));
        CumulativeRaffleDbo cumulativeRaffleDbo = cumulativeRaffleDboDao.findByMemberIdAndTime(memberId, formatTime);

        if (cumulativeRaffleDbo == null) {
            cumulativeRaffleDbo = new CumulativeRaffleDbo();
            cumulativeRaffleDbo.setCumulativeDay(2);
            cumulativeRaffleDbo.setMemberId(memberId);
            cumulativeRaffleDbo.setTime(formatTime);
            cumulativeRaffleDbo.setId(memberId+"_"+formatTime+"_extraReward");
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DAY_OF_WEEK, -1);

            cumulativeRaffleDbo.setLastRaffleDay(c.getTime().getTime());//currenttimr

            cumulativeRaffleDboDao.save(cumulativeRaffleDbo);
            return null;
        } else {

            //如果今天已经+1过了
            if (hasAddToday(cumulativeRaffleDbo.getLastRaffleDay())) {
                return null;
            }

            LotteryDbo lotteryDbo = null;
            Integer cumulativeDay = cumulativeRaffleDbo.getCumulativeDay() + 1;
            cumulativeRaffleDbo.setCumulativeDay(cumulativeDay);
            cumulativeRaffleDbo.setLastRaffleDay(currentTime);

            if (cumulativeDay >= 3) {

                List<LotteryDbo> extraLottey = lotteryDboDao.findExtraLottey();
                if (extraLottey == null || extraLottey.size() < 5) {
                    return null;//防止空指针异常
                }

                List<Integer> hasRewarded = cumulativeRaffleDbo.getHasRewarded();
                List<LotteryDbo> extraRecordList = cumulativeRaffleDbo.getExtraRecordList();
                if (hasRewarded == null) {
                    hasRewarded = new ArrayList<>();
                }
                if (extraRecordList == null) {
                    extraRecordList = new ArrayList<>();
                }

                if (cumulativeDay == 3) {
                    hasRewarded.add(3);
                    extraRecordList.add(extraLottey.get(0));
                    lotteryDbo = extraLottey.get(0);
                } else if (cumulativeDay == 5) {
                    hasRewarded.add(5);
                    extraRecordList.add(extraLottey.get(1));
                    lotteryDbo = extraLottey.get(1);
                } else if (cumulativeDay == 7) {
                    hasRewarded.add(7);
                    extraRecordList.add(extraLottey.get(2));
                    lotteryDbo = extraLottey.get(2);
                } else if (cumulativeDay == 15) {
                    hasRewarded.add(15);
                    extraRecordList.add(extraLottey.get(3));
                    lotteryDbo = extraLottey.get(3);
                } else if (cumulativeDay == 25) {
                    hasRewarded.add(25);
                    extraRecordList.add(extraLottey.get(4));
                    lotteryDbo = extraLottey.get(4);
                } else {
                    return null;
                }
                cumulativeRaffleDbo.setHasRewarded(hasRewarded);
                cumulativeRaffleDbo.setExtraRecordList(extraRecordList);
            }
            cumulativeRaffleDboDao.save(cumulativeRaffleDbo);
            return lotteryDbo;
        }
    }

    private boolean hasAddToday(Long time) {
        final long lastSignTimeAsDay = time / Constant.ONE_DAY_MS;
        final long currentSignTimeAsDay = System.currentTimeMillis() / Constant.ONE_DAY_MS;
        return (lastSignTimeAsDay == currentSignTimeAsDay);
    }
}