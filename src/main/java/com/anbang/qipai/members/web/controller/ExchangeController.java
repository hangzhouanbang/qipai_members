package com.anbang.qipai.members.web.controller;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.domain.prize.ExchangeRecord;
import com.anbang.qipai.members.cqrs.c.domain.prize.ExchangeException;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.cqrs.c.service.MemberHongBaoCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberPhoneFeeCmdService;
import com.anbang.qipai.members.cqrs.q.dbo.MemberHongBaoRecordDbo;
import com.anbang.qipai.members.cqrs.q.dbo.PhoneFeeRecordDbo;
import com.anbang.qipai.members.cqrs.q.dbo.ScoreExchangeRecordDbo;
import com.anbang.qipai.members.cqrs.q.service.HongBaoQueryService;
import com.anbang.qipai.members.cqrs.q.service.PhoneFeeQueryService;
import com.anbang.qipai.members.enums.ExchangeType;
import com.anbang.qipai.members.msg.service.PrizeLogMsgService;
import com.anbang.qipai.members.web.vo.CommonVO;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    @Autowired
    private MemberAuthService memberAuthService;

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


}
