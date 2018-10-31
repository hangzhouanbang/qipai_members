package com.anbang.qipai.members.web.controller;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.domain.prize.ObatinSigningPrizeRecord;
import com.anbang.qipai.members.cqrs.c.domain.sign.OpportunityInvalidUsedException;
import com.anbang.qipai.members.cqrs.c.domain.sign.OpportunityNotExistsExcetion;
import com.anbang.qipai.members.cqrs.c.domain.sign.SignTypeEnum;
import com.anbang.qipai.members.cqrs.c.service.*;
import com.anbang.qipai.members.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberGoldQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberScoreQueryService;
import com.anbang.qipai.members.cqrs.q.service.ObtainPrizeQueryService;
import com.anbang.qipai.members.msg.service.PrizeLogMsgService;
import com.anbang.qipai.members.web.vo.CommonVO;
import com.anbang.qipai.members.web.vo.ObtainPrizeVo;
import com.anbang.qipai.members.web.vo.ObtainSigningPrizeRecordVo;
import com.dml.accounting.AccountingRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static com.anbang.qipai.members.cqrs.c.domain.sign.SignTypeEnum.*;

@RestController
@RequestMapping("/prize")
public class PrizeController {


    @Autowired
    private MemberGoldCmdService memberGoldCmdService;

    @Autowired
    private MemberCardCmdService memberCardCmdService;

    @Autowired
    private MemberAuthService memberAuthService;

    @Autowired
    private MemberGoldQueryService memberGoldQueryService;

    @Autowired
    private MemberAuthQueryService memberAuthQueryService;

    @Autowired
    private ObtainPrizeQueryService obtainPrizeQueryService;

    @Autowired
    private PrizeLogMsgService prizeLogMsgService;

    @Autowired
    private MemberScoreCmdService memberScoreCmdService;

    @Autowired
    private MemberScoreQueryService memberScoreQueryService;

    @ResponseBody
    @RequestMapping("/obtain")
    public CommonVO obtain(String token, int day) {
        final String memberId = this.memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            return new CommonVO(false, "用户未登陆", null);
        }

        SignTypeEnum signTypeEnum = SignTypeEnum.of(day);
        if (signTypeEnum == null) {
            return new CommonVO(false, "该签到天数无奖励类型对应", null);
        }

        ObatinSigningPrizeRecord record = null;
        try {
            if (signTypeEnum == FIVE || signTypeEnum == FIFTEEN || signTypeEnum == TWENTY) {
                record = this.memberGoldCmdService.obtainSignPrizeGold(memberId, signTypeEnum);
                AccountingRecord accountingRecord = this.memberGoldCmdService.giveGoldToMember(memberId, record.getPrize().getNum(), "签到奖励发放 玉石*" + record.getPrize().getNum(), System.currentTimeMillis());
                record.setGoldAccountingRecord(accountingRecord);
                this.memberGoldQueryService.withdraw(memberId, accountingRecord);
                this.obtainPrizeQueryService.save(record);
            } else {
                record = this.memberCardCmdService.obtainMemberCardSignPrize(memberId, signTypeEnum);
                this.memberAuthQueryService.prolongPrizeVipTime(memberId, record.getPrize());
                this.obtainPrizeQueryService.save(record);
            }
            if (record.getVipLevel() > 0) {
                AccountingRecord accountingRecord = memberScoreCmdService.giveScoreToMember(memberId, record.getScore(), "会员签到奖励礼券", System.currentTimeMillis());
                memberScoreQueryService.withdraw(memberId, accountingRecord);
                record.setScoreAccountingRecord(accountingRecord);
            }
            this.prizeLogMsgService.sendObtainSignPrize(record);
        } catch (OpportunityInvalidUsedException e) {
            return new ObtainPrizeVo(false, e.getMessage(), null, true);
        } catch (MemberNotFoundException e) {
            return new CommonVO(false, "账户不存在", null);
        } catch (OpportunityNotExistsExcetion e) {
            return new CommonVO(false, e.getMessage(), null);
        }
        return new CommonVO(true, null, new ObtainSigningPrizeRecordVo(record));
    }



}
