package com.anbang.qipai.members.web.controller;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.domain.prize.ExchangeRecord;
import com.anbang.qipai.members.cqrs.c.domain.prize.ExchangeException;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.cqrs.c.service.MemberHongBaoCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberPhoneFeeCmdService;
import com.anbang.qipai.members.cqrs.q.dao.MemberDboDao;
import com.anbang.qipai.members.cqrs.q.dao.mongodb.MongodbMemberDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.*;
import com.anbang.qipai.members.cqrs.q.service.*;
import com.anbang.qipai.members.enums.ExchangeType;
import com.anbang.qipai.members.msg.service.PrizeLogMsgService;
import com.anbang.qipai.members.web.vo.CommonVO;
import com.anbang.qipai.members.web.vo.EntityExchangeDO;
import com.anbang.qipai.members.web.vo.RaffleHistoryVO;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    @Autowired
    private MemberAuthService memberAuthService;

    @Autowired
    private MemberAuthQueryService memberAuthQueryService;

    @Autowired
    private MemberHongBaoCmdService memberHongBaoCmdService;

    @Autowired
    private HongBaoQueryService hongBaoQueryService;

    @Autowired
    private PrizeLogMsgService prizeLogMsgService;

    @Autowired
    private MemberPhoneFeeCmdService memberPhoneFeeCmdService;

    @Autowired
    private PhoneFeeQueryService phoneFeeQueryService;

    @Autowired
    private MemberRaffleQueryService memberRaffleQueryService;

    @Autowired
    private ReceiverInfoQueryService receiverInfoQueryService;


    @RequestMapping("/hongbao")
    @ResponseBody
    public CommonVO exchangeHongBao(String token, int score, String phone) {
        final String memberId = this.memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            return new CommonVO(false, "用户未登陆", null);
        }
        try {
            ExchangeRecord exchangeRecord = this.memberHongBaoCmdService.exchange(memberId, score, "红包兑换 * " + score, System.currentTimeMillis());
            AccountingRecord accountingRecord = exchangeRecord.getRecord();
            MemberHongBaoRecordDbo hongBaoRecordDbo = this.hongBaoQueryService.withdraw(memberId, accountingRecord);
            ScoreExchangeRecordDbo scoreExchangeRecordDbo = new ScoreExchangeRecordDbo(hongBaoRecordDbo.getId(),
                    memberId,
                    phone,
                    hongBaoRecordDbo.getTime(),
                    (int) hongBaoRecordDbo.getAccountingAmount(),
                    exchangeRecord.getConcurrency(),
                    ExchangeType.HONG_BAO,
                    (int) hongBaoRecordDbo.getBalanceAfter(),
                    accountingRecord);
            this.prizeLogMsgService.sendExchangeLog(scoreExchangeRecordDbo);
            return new CommonVO(true, null, scoreExchangeRecordDbo);
        } catch (InsufficientBalanceException e) {
            return new CommonVO(false, "积分不足", null);
        } catch (MemberNotFoundException e) {
            return new CommonVO(false, "账户不存在", null);
        } catch (ExchangeException e) {
            return new CommonVO(false, e.getMessage(), null);
        }
    }

    @RequestMapping("/phonefee")
    @ResponseBody
    public CommonVO exchangePhoneFee(String token, int score, String phone) {
        final String memberId = this.memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            return new CommonVO(false, "用户未登陆", null);
        }
        try {
            ExchangeRecord exchangeRecord = this.memberPhoneFeeCmdService.exchange(memberId, score, "用户话费兑换 *" + score, System.currentTimeMillis());
            AccountingRecord accountingRecord = exchangeRecord.getRecord();
            PhoneFeeRecordDbo phoneFeeRecordDbo = phoneFeeQueryService.withdraw(memberId, accountingRecord);
            ScoreExchangeRecordDbo scoreExchangeRecordDbo = new ScoreExchangeRecordDbo(phoneFeeRecordDbo.getId(),
                    memberId,
                    phone,
                    phoneFeeRecordDbo.getTime(),
                    (int) phoneFeeRecordDbo.getAccountingAmount(),
                    exchangeRecord.getConcurrency(),
                    ExchangeType.PHONE_FEE,
                    (int) phoneFeeRecordDbo.getBalanceAfter(), accountingRecord);
            this.prizeLogMsgService.sendExchangeLog(scoreExchangeRecordDbo);
            return new CommonVO(true, null, scoreExchangeRecordDbo);
        } catch (InsufficientBalanceException e) {
            return new CommonVO(false, "积分不足", null);
        } catch (MemberNotFoundException e) {
            return new CommonVO(false, "账户不存在", null);
        } catch (ExchangeException e) {
            return new CommonVO(false, e.getMessage(), null);
        }
    }

    @RequestMapping(value = {"/memberrafflehistory"})
    @ResponseBody
    public CommonVO memberRaffleHistory(String token) {

        CommonVO vo = new CommonVO();
        String memberId = memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            vo.setSuccess(false);
            vo.setMsg("invalid token");
            return vo;
        }
        List<RaffleHistoryVO> raffleHistoryVOList = new ArrayList<>();

        List<MemberRaffleHistoryDbo> histories = memberRaffleQueryService.findHistoriesWithoutPage(memberId);

        for (MemberRaffleHistoryDbo memberRaffleHistoryDbo : histories) {
            RaffleHistoryVO resultVo = new RaffleHistoryVO();

            //如果是实体的话 需要判断有没有兑换过
            if (memberRaffleHistoryDbo.getLottery().getType().name().equals("ENTIRY")) {

                //如果该字段没有记录的话 说明没有兑换过
                if (StringUtils.isEmpty(memberRaffleHistoryDbo.getHasExchange())) {
                    resultVo.setHasExchange("NO");
                } else {
                    resultVo.setHasExchange(memberRaffleHistoryDbo.getHasExchange());
                }
            }
            resultVo.setId(memberRaffleHistoryDbo.getId());
            resultVo.setLotteryId(memberRaffleHistoryDbo.getLottery().getId());
            resultVo.setLotteryName(memberRaffleHistoryDbo.getLottery().getName());
            resultVo.setType(memberRaffleHistoryDbo.getLottery().getType().name());
            resultVo.setTime(memberRaffleHistoryDbo.getTime());
            resultVo.setMemberId(memberRaffleHistoryDbo.getMemberId());
            resultVo.setSingleNum(String.valueOf(memberRaffleHistoryDbo.getLottery().getSingleNum()));
            raffleHistoryVOList.add(resultVo);
        }
        vo.setSuccess(true);
        vo.setData(raffleHistoryVOList);
        return vo;
    }

    @RequestMapping(value = {"/addinformation"})
    @ResponseBody
    public CommonVO addInformation(String token, ReceiverInfoDbo receiverInfoDbo) {
        CommonVO vo = new CommonVO();

        String memberId = memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            vo.setSuccess(false);
            vo.setMsg("invalid token");
            return vo;
        }

        if (StringUtils.isEmpty(receiverInfoDbo.getName()) ||
                StringUtils.isEmpty(receiverInfoDbo.getAddress()) ||
                StringUtils.isEmpty(receiverInfoDbo.getTelephone())) {
            vo.setSuccess(false);
            vo.setMsg("手机号、收件地址、收货人不得为空");
            return vo;
        }
        //手机号11位
        if (receiverInfoDbo.getTelephone().length() != 11) {
            vo.setSuccess(false);
            vo.setMsg("手机号格式不正确");
        }
        receiverInfoDbo.setId(memberId + "_receiverInfo");
        receiverInfoDbo.setAddress(receiverInfoDbo.getAddress());
        receiverInfoDbo.setGender(receiverInfoDbo.getGender());
        receiverInfoDbo.setName(receiverInfoDbo.getName());
        receiverInfoDbo.setTelephone(receiverInfoDbo.getTelephone());
        receiverInfoDbo.setMemberId(memberId);
        receiverInfoQueryService.addReceiverInfo(receiverInfoDbo);
        vo.setSuccess(true);
        vo.setMsg("添加收件人信息成功");
        return vo;
    }


    @RequestMapping("/entity")
    @ResponseBody
    public CommonVO exchangeEntity(String token, String entityId) {
        final String memberId = this.memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            return new CommonVO(false, "用户未登陆", null);
        }
        ReceiverInfoDbo receiverInfo = receiverInfoQueryService.findReceiverByMemberId(memberId);
        if (receiverInfo == null) {
            return new CommonVO(false, "请先填写收货人信息", null);
        }
        if (StringUtils.isEmpty(entityId)) {
            return new CommonVO(false, "中奖记录ID不得为空", null);
        }
        MemberRaffleHistoryDbo raffleHistoryDbo = memberRaffleQueryService.find(entityId);

        if (raffleHistoryDbo == null) {
            return new CommonVO(false, "无该条中奖记录", null);
        }
        EntityExchangeDO entityExchangeDO = new EntityExchangeDO();
        String nickName = memberAuthQueryService.findNameByMemberID(memberId);

        entityExchangeDO.setMemberId(memberId);
        entityExchangeDO.setNickName(nickName);
        entityExchangeDO.setTelephone(receiverInfo.getTelephone());
        entityExchangeDO.setAddress(receiverInfo.getAddress());
        entityExchangeDO.setExchangeTime(System.currentTimeMillis());
        entityExchangeDO.setLotteryName(raffleHistoryDbo.getLottery().getName());
        entityExchangeDO.setLotteryType(raffleHistoryDbo.getLottery().getType().name());
        entityExchangeDO.setSingleNum(String.valueOf(raffleHistoryDbo.getLottery().getSingleNum()));

        raffleHistoryDbo.setHasExchange("WAIT");
        memberRaffleQueryService.save(raffleHistoryDbo);
        //TODO:发送消息
        this.prizeLogMsgService.sendEntityExchangeLog(entityExchangeDO);

        return new CommonVO(true, null, entityExchangeDO);
    }

    @RequestMapping(value = {"/queryphonefeeAndHongbao"})
    @ResponseBody
    public CommonVO queryPhoneFee(String token) {
        final String memberId = this.memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            return new CommonVO(false, "用户未登陆", null);
        }
        double phoneFee = phoneFeeQueryService.find(memberId);


        double hongBao = hongBaoQueryService.find(memberId);


        Map<String, String> resultMap = new HashMap<>();


        resultMap.put("phone", String.valueOf(phoneFee));
        resultMap.put("hongbao", String.valueOf(hongBao));

        CommonVO commonVO = new CommonVO();
        commonVO.setSuccess(true);
        commonVO.setData(resultMap);
        return commonVO;
    }


    @RequestMapping(value = {"/queryreceiverinfo"})
    @ResponseBody
    public CommonVO queryReceiverInfo(String token) {
        final String memberId = this.memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            return new CommonVO(false, "用户未登陆", null);
        }
        ReceiverInfoDbo receiver = receiverInfoQueryService.findReceiverByMemberId(memberId);
        if(receiver == null){
            return new CommonVO(false,"请先填写收货人信息",null);
        }
        CommonVO vo = new CommonVO();
        vo.setSuccess(true);
        vo.setData(receiver);
        return vo;
    }


}
