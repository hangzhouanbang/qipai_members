package com.anbang.qipai.members.web.controller;

import com.anbang.qipai.members.cqrs.c.domain.prize.LotteryTypeEnum;
import com.anbang.qipai.members.cqrs.c.domain.prize.LotteryValueObject;
import com.anbang.qipai.members.cqrs.c.domain.prize.RaffleHistoryValueObject;
import com.anbang.qipai.members.cqrs.c.domain.sign.Constant;
import com.anbang.qipai.members.cqrs.c.domain.sign.SignHistoryValueObject;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.cqrs.c.service.MemberGoldCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberHongBaoCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberPhoneFeeCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberPrizeCmdService;
import com.anbang.qipai.members.cqrs.c.service.SignCmdService;
import com.anbang.qipai.members.cqrs.q.dbo.Lottery;
import com.anbang.qipai.members.cqrs.q.dbo.LotteryDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberRaffleHistoryDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberSignCountDbo;
import com.anbang.qipai.members.cqrs.q.service.HongBaoQueryService;
import com.anbang.qipai.members.cqrs.q.service.LotteryQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberGoldQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberRaffleQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberSignQueryService;
import com.anbang.qipai.members.cqrs.q.service.PhoneFeeQueryService;
import com.anbang.qipai.members.enums.ExtraRaffle;
import com.anbang.qipai.members.exception.AnBangException;
import com.anbang.qipai.members.msg.service.PrizeLogMsgService;
import com.anbang.qipai.members.plan.bean.HasRaffle;
import com.anbang.qipai.members.plan.service.MemberGradeInFoService;
import com.anbang.qipai.members.web.vo.*;
import com.dml.accounting.AccountingRecord;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sign")
public class SignController {

    @Autowired
    private SignCmdService signCmdService;

    @Autowired
    private MemberSignQueryService memberSignQueryService;

    @Autowired
    private MemberPrizeCmdService memberPrizeCmdService;

    @Autowired
    private MemberAuthService memberAuthService;

    @Autowired
    private LotteryQueryService lotteryQueryService;

    @Autowired
    private MemberGoldCmdService memberGoldCmdService;

    @Autowired
    private MemberHongBaoCmdService memberHongBaoCmdService;

    @Autowired
    private HongBaoQueryService hongBaoQueryService;

    @Autowired
    private MemberGoldQueryService memberGoldQueryService;

    @Autowired
    private PhoneFeeQueryService phoneFeeQueryService;

    @Autowired
    private MemberPhoneFeeCmdService memberPhoneFeeCmdService;

    @Autowired
    private MemberAuthQueryService memberAuthQueryService;

    @Autowired
    private MemberRaffleQueryService memberRaffleQueryService;

    @Autowired
    private PrizeLogMsgService prizeLogMsgService;

    @Autowired
    private MemberGradeInFoService memberGradeInFoService;

    @ResponseBody
    @RequestMapping("/today")
    public SignVo sign(String token) {
        final String memberId = memberAuthService.getMemberIdBySessionId(token);
        if (this.memberSignQueryService.isSignedToday(memberId)) {
            return new SignVo(false, "您今天已经签过到");
        } else {
            SignHistoryValueObject signHistory = this.signKernal(memberId);
            SignVo signVo = new SignVo(true, null);
            signVo.addData("signHistory", signHistory);
            MemberSignCountDbo memberSignCountDbo = this.memberSignQueryService.find(memberId);
            int days = memberSignCountDbo.getDays();


            //签到不抽奖
            RaffleHistoryVO raffleResult = new RaffleHistoryVO();
//            try {
//                final RaffleHistoryVO raffle = this.raffle(memberId);
//                raffleResult = raffle;
//            } catch (Exception e) {
//                return new SignVo(false, e.getMessage());
//            }
            MemberDbo memberDbo = memberAuthQueryService.findMemberById(memberId);
            int level = memberDbo.getVipLevel();
            signVo.addData("vipLevel", level);
            signVo.addData("signCount", days);
            signVo.addData("MSG", "签到成功");
//            signVo.addData("LotteryID", raffleResult.getLotteryId());
//            signVo.addData("LotteryName", raffleResult.getLotteryName());
//            signVo.addData("LotteryType", raffleResult.getType());
            return signVo;
        }
    }

    /**
     * 执行真正签到逻辑
     */
    private SignHistoryValueObject signKernal(String memberId) {
        final MemberDbo memberDbo = this.memberAuthQueryService.findMemberById(memberId);
        final int vipLevel = memberDbo.getVipLevel();
        final SignHistoryValueObject signHistory = this.signCmdService.sign(memberId, vipLevel);
        final MemberSignCountDbo current = new MemberSignCountDbo(memberId, signHistory.getContinuousSignDays(), signHistory.getTime());
        this.memberSignQueryService.saveOrUpdateMemberSignCount(current);

        //每日更新分享可获取抽奖的机会
        MemberRaffleHistoryDbo raffleHistoryDbo = memberRaffleQueryService.find(memberId);
        if (raffleHistoryDbo != null) {
            raffleHistoryDbo.setExtraRaffle(ExtraRaffle.NO.name());
            memberRaffleQueryService.save(raffleHistoryDbo);
        }
        return signHistory;
    }

    @RequestMapping("/findlotterylist")
    @ResponseBody
    public CommonVO findLotteryList() {
        CommonVO vo = new CommonVO();
        List<LotteryVo> lotteryList = list();
        vo.setSuccess(true);
        vo.setData(lotteryList);
        return vo;
    }


    /**
     * 获取转盘上十个物品
     *
     * @return
     */
    private List<LotteryVo> list() {
        final List<LotteryDbo> lotteryDboList = this.lotteryQueryService.findAll();
        List<LotteryVo> lotteryVoList = new ArrayList<>();
        for (LotteryDbo lotteryDbo : lotteryDboList) {
            LotteryVo lotteryVo = new LotteryVo(lotteryDbo.getId(), lotteryDbo.getIcon(),
                    lotteryDbo.getName(), lotteryDbo.getType().toString(), lotteryDbo.getCardType());
            lotteryVoList.add(lotteryVo);
        }
        return lotteryVoList;
    }

    /**
     * 抽奖
     */
    private CommonVO raffle(String memberId) throws Exception {
        CommonVO commonVO = new CommonVO();
        if (!memberPrizeCmdService.isRaffleTableInitalized()) {
            commonVO.setSuccess(false);
            commonVO.setMsg("抽奖暂时未开放");
            return commonVO;
        }
        HasRaffle hasRaffle = isRaffleToday(memberId);

        //短路与  如果为空直接返回false
        if (hasRaffle.isRaffleToday() && (!StringUtils.isEmpty(hasRaffle.getExtraRaffle()))
                & (!hasRaffle.getExtraRaffle().equals(ExtraRaffle.YES.name()))) {
            commonVO.setSuccess(false);
            commonVO.setMsg("您今天的抽奖次数已经用完");
            return commonVO;
        }
        boolean isFirst = this.memberRaffleQueryService.isFirstRaffle(memberId);
        try {
            RaffleHistoryValueObject raffleHistoryValueObject = null;
            raffleHistoryValueObject = this.memberPrizeCmdService.raffle(memberId, isFirst);
            LotteryValueObject lottery = raffleHistoryValueObject.getLottery();
            final LotteryTypeEnum lotteryType = lottery.getType();
            if (lotteryType == LotteryTypeEnum.HONGBAO) {
                AccountingRecord record = this.memberHongBaoCmdService.giveHongBaoToMember(memberId,
                        lottery.getSingleNum(),
                        "抽奖，红包*" + lottery.getSingleNum(),
                        raffleHistoryValueObject.getTime());
                this.hongBaoQueryService.withdraw(memberId, record);
            } else if (lotteryType == LotteryTypeEnum.GOLD) {
                AccountingRecord record = this.memberGoldCmdService.giveGoldToMember(memberId,
                        lottery.getSingleNum(),
                        "抽奖，玉石*" + lottery.getSingleNum(),
                        System.currentTimeMillis());
                this.memberGoldQueryService.withdraw(memberId, record);
            } else if (LotteryTypeEnum.isMemberCard(lotteryType)) {
                this.memberAuthQueryService.prolongVipTimeByRaffle(memberId,
                        lotteryType,
                        lottery.getSingleNum());
            } else if (lotteryType == LotteryTypeEnum.PHONE_FEE) {
                AccountingRecord record = this.memberPhoneFeeCmdService.givePhoneFeeToMember(memberId,
                        lottery.getSingleNum(),
                        "抽奖，话费*" + lottery.getSingleNum(),
                        System.currentTimeMillis());
                this.phoneFeeQueryService.withdraw(memberId, record);
            } else if (lotteryType == LotteryTypeEnum.ENTIRY) {
                //领取的实物,本地微服务什么都不做
                //抽到谢谢惠顾也什么都不做
            }
            Lottery lotDbo = new Lottery(lottery.getId(), lottery.getName(), lottery.getProp(), lottery.getFirstProp(), lotteryType, lottery.getSingleNum());
            MemberRaffleHistoryDbo memberRaffleHistoryDbo = new MemberRaffleHistoryDbo(null, memberId, lotDbo, null, raffleHistoryValueObject.getTime(), raffleHistoryValueObject.isFirstTime());
            if (hasRaffle.isRaffleToday() && hasRaffle.getExtraRaffle().equals(ExtraRaffle.YES.name())) {
                //把上一次为YES的状态也改为USED
                MemberRaffleHistoryDbo lastRaffleHistory = memberRaffleQueryService.findByMemberId(memberId);
                lastRaffleHistory.setExtraRaffle(ExtraRaffle.USED.name());
                memberRaffleQueryService.save(lastRaffleHistory);

                //改完后再保存这一次的抽奖  也为Used
                memberRaffleHistoryDbo.setExtraRaffle(ExtraRaffle.USED.name());
            }
            memberRaffleQueryService.save(memberRaffleHistoryDbo);
            this.prizeLogMsgService.sendRaffleRecord(memberRaffleHistoryDbo);
            RaffleHistoryVO raffleHistoryVO = new RaffleHistoryVO(memberRaffleHistoryDbo.getId(),
                    raffleHistoryValueObject.getMemberId(),
                    raffleHistoryValueObject.getLottery().getId(),
                    raffleHistoryValueObject.getLottery().getName(),
                    memberRaffleHistoryDbo.getLottery().getType().name(),
                    raffleHistoryValueObject.getTime(),
                    raffleHistoryValueObject.isFirstTime());
            commonVO.setMsg("抽奖成功");
            commonVO.setData(raffleHistoryVO);
            return commonVO;
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping("/sign_view")
    public SignVo signView(String token) {
        final String memberId = memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            return new SignVo(false, "用户未登陆");
        }
        List<LotteryVo> list = this.list();
        SignVo signVo = new SignVo(true, null);

        signVo.addData("lotteryTable", list);
        MemberSignCountDbo memberSignCountDbo = this.memberSignQueryService.find(memberId);
        int days = memberSignCountDbo.getDays();
//        List<Integer> day = new ArrayList<>();
//        if (days <= 3)
//            day.add(3);
//        if (days <= 5)
//            day.add(5);
//        if (days <= 7)
//            day.add(7);
//        if (days <= 15)
//            day.add(15);
//        if (days <= 20)
//            day.add(20);
//        signVo.addData("days", day);
        double phoneFee = this.phoneFeeQueryService.find(memberId);
        signVo.addData("phoneFee", phoneFee);
        double hongbao = this.hongBaoQueryService.find(memberId);
        signVo.addData("hongbao", hongbao);
        GradeVo gradeVo = this.memberGradeInFoService.find_grade_info(memberId);
        signVo.addData("shortage", gradeVo.getShortage());
        return signVo;
    }


    @ResponseBody
    @RequestMapping("/check_sign_tody")
    public CommonVO isSignToday(String token) {
        final String memberId = memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            return new CommonVO(false, "用户未登陆", null);
        }
        if (this.memberSignQueryService.isSignedToday(memberId)) {
            return new CommonVO(true, null, null);
        } else {
            return new CommonVO(false, null, null);
        }
    }

    @RequestMapping("/continuous_day")
    @ResponseBody
    public CommonVO continuousDays(String token) {
        final String memberId = memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            return new CommonVO(false, "用户未登陆", null);
        }
        final MemberSignCountDbo memberSignCountDbo = this.memberSignQueryService.find(memberId);
        if (memberSignCountDbo == null) {
            return new CommonVO(true, "", 0);
        }
        return new CommonVO(true, "", memberSignCountDbo.getDays());
    }


    //判断今天是否抽过奖
    private HasRaffle isRaffleToday(String memberID) {
        HasRaffle result = new HasRaffle();
        MemberRaffleHistoryDbo historyDbo = this.memberRaffleQueryService.findByMemberId(memberID);

        if (historyDbo == null) {
            //防止直接请求接口的风险
            MemberDbo member = memberAuthQueryService.findMemberById(memberID);
            if (member == null) {
                throw new AnBangException("用户不存在，请先注册");
            }

            result.setRaffleToday(false);
            return result;
        } else {
            result.setExtraRaffle(historyDbo.getExtraRaffle());
            final long lastRaffleTimeAsDay = historyDbo.getTime() / Constant.ONE_DAY_MS;
            final long currentTimeAsDay = System.currentTimeMillis() / Constant.ONE_DAY_MS;
            result.setRaffleToday(lastRaffleTimeAsDay == currentTimeAsDay);
            return result;
        }
    }


    //分享 奖励1次抽奖
    @RequestMapping("/rewardRaffle")
    @ResponseBody
    public CommonVO rewardOnceRaffle(String token, boolean hasShared) {
        final String memberId = memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            return new CommonVO(false, "用户未登陆", null);
        }

        CommonVO commonVO;
        if (!hasShared) {
            commonVO = new CommonVO(false, "请先分享", "");
            return commonVO;
        }

        MemberRaffleHistoryDbo raffleHistoryDbo = memberRaffleQueryService.findByMemberId(memberId);

        //在签到时已经会更新一次 当日可分享领取
        // 为防止用户 不签到就分享  在这里再更新一次
        final long lastRaffleTimeAsDay = raffleHistoryDbo.getTime() / Constant.ONE_DAY_MS;
        final long currentTimeAsDay = System.currentTimeMillis() / Constant.ONE_DAY_MS;
        if (lastRaffleTimeAsDay != currentTimeAsDay) {
            raffleHistoryDbo.setExtraRaffle(ExtraRaffle.NO.name());
            memberRaffleQueryService.save(raffleHistoryDbo);
        }

        if (raffleHistoryDbo.getExtraRaffle().equals(ExtraRaffle.NO.name())) {
            raffleHistoryDbo.setExtraRaffle(ExtraRaffle.YES.name());
            memberRaffleQueryService.save(raffleHistoryDbo);
            commonVO = new CommonVO(true, "恭喜你获得了一次抽奖机会", "");
            return commonVO;
        }

        commonVO = new CommonVO(false, "请明天再来", "");
        return commonVO;
    }


    //抽奖的接口
    @RequestMapping(value = {"/raffle"})
    @ResponseBody
    public CommonVO tryRaffle(String token) {
        final String memberId = memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            return new CommonVO(false, "用户未登陆", null);
        }

        try {
            return raffle(memberId);
        } catch (Exception e) {
            throw new AnBangException(e.getMessage(), e);
        }
    }

    @RequestMapping(value = {"queryraffle"})
    @ResponseBody
    public CommonVO queryRaffle(String token) {

        final String memberId = memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            return new CommonVO(false, "用户未登陆", null);
        }

        //签到记录
        MemberSignCountDbo signCountDbo = memberSignQueryService.find(memberId);
        final long lastSignTime = signCountDbo.getLastSignTime() / Constant.ONE_DAY_MS;
        final long currentSignTime = System.currentTimeMillis() / Constant.ONE_DAY_MS;


        HasRaffle hasRaffle = isRaffleToday(memberId);
        int count = 0;
        //如果今天已经签到  并且 没有抽奖的话
        if ((!hasRaffle.isRaffleToday()) && (lastSignTime == currentSignTime)) {
            count++;
        } else {

        }

        if ((!StringUtils.isEmpty(hasRaffle.getExtraRaffle()))) {

            if (hasRaffle.getExtraRaffle().equals(ExtraRaffle.NO.name())) {
                count++;
            }
        }

        CommonVO commonVO = new CommonVO();
        commonVO.setSuccess(true);
        commonVO.setMsg(String.valueOf(count));
        return commonVO;
    }
}
