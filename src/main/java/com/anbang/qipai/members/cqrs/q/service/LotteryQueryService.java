package com.anbang.qipai.members.cqrs.q.service;

import com.anbang.qipai.members.cqrs.c.domain.prize.LotteryTypeEnum;
import com.anbang.qipai.members.cqrs.c.domain.sign.Constant;
import com.anbang.qipai.members.cqrs.c.service.MemberGoldCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberHongBaoCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberPhoneFeeCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberPrizeCmdService;
import com.anbang.qipai.members.cqrs.q.dao.CumulativeRaffleDboDao;
import com.anbang.qipai.members.cqrs.q.dao.LotteryDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.CumulativeRaffleDbo;
import com.anbang.qipai.members.cqrs.q.dbo.Lottery;
import com.anbang.qipai.members.cqrs.q.dbo.LotteryDbo;
import com.anbang.qipai.members.web.vo.CommonVO;
import com.dml.accounting.AccountingRecord;
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
    private MemberGoldCmdService memberGoldCmdService;

    @Autowired
    private MemberHongBaoCmdService memberHongBaoCmdService;

    @Autowired
    private HongBaoQueryService hongBaoQueryService;

    @Autowired
    private MemberGoldQueryService memberGoldQueryService;

    @Autowired
    private MemberPhoneFeeCmdService memberPhoneFeeCmdService;

    @Autowired
    private MemberAuthQueryService memberAuthQueryService;

    @Autowired
    private PhoneFeeQueryService phoneFeeQueryService;

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


    public String findIconByLotteryId(String id) {
        return lotteryDboDao.findLotteryById(id).getIcon();
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
            cumulativeRaffleDbo.setCumulativeDay(1);
            cumulativeRaffleDbo.setMemberId(memberId);
            cumulativeRaffleDbo.setTime(formatTime);
            cumulativeRaffleDbo.setId(memberId + "_" + formatTime + "_extraReward");

//            Calendar c = Calendar.getInstance();
//            c.setTime(new Date());
//            c.add(Calendar.DAY_OF_WEEK, -1);
//            currentTime
            cumulativeRaffleDbo.setLastRaffleDay(currentTime);//currenttimr

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

                //TODO:
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
                }

                if (lotteryDbo != null) {
                    try {
                        final LotteryTypeEnum lotteryType = lotteryDbo.getType();
                        if (lotteryType == LotteryTypeEnum.HONGBAO) {
                            AccountingRecord record = this.memberHongBaoCmdService.giveHongBaoToMember(memberId,
                                    lotteryDbo.getSingleNum(), "抽奖，红包*" + lotteryDbo.getSingleNum(), currentTime);
                            this.hongBaoQueryService.withdraw(memberId, record);
                        } else if (lotteryType == LotteryTypeEnum.GOLD) {
                            AccountingRecord record = this.memberGoldCmdService.giveGoldToMember(memberId, lotteryDbo.getSingleNum(),
                                    "抽奖，玉石*" + lotteryDbo.getSingleNum(), System.currentTimeMillis());
                            this.memberGoldQueryService.withdraw(memberId, record);
                        } else if (LotteryTypeEnum.isMemberCard(lotteryType)) {
                            this.memberAuthQueryService.prolongVipTimeByRaffle(memberId, lotteryType, lotteryDbo.getSingleNum());
                        } else if (lotteryType == LotteryTypeEnum.PHONE_FEE) {
                            AccountingRecord record = this.memberPhoneFeeCmdService.givePhoneFeeToMember(memberId,
                                    lotteryDbo.getSingleNum(), "抽奖，话费*" + lotteryDbo.getSingleNum(), System.currentTimeMillis());
                            this.phoneFeeQueryService.withdraw(memberId, record);
                        } else if (lotteryType == LotteryTypeEnum.ENTIRY) {
                            // 领取的实物,本地微服务什么都不做
                            // 抽到谢谢惠顾也什么都不做
                        }
                    } catch (Exception e) {
                        return null;             //一旦写内存失败 直接返回
                    }
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