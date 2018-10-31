package com.anbang.qipai.members.web.controller;

import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.cqrs.c.service.MemberGoldCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberHongBaoCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberPhoneFeeCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberPrizeCmdService;
import com.anbang.qipai.members.cqrs.q.dbo.LotteryDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberRaffleHistoryDbo;
import com.anbang.qipai.members.cqrs.q.service.HongBaoQueryService;
import com.anbang.qipai.members.cqrs.q.service.LotteryQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberGoldQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberRaffleQueryService;
import com.anbang.qipai.members.cqrs.q.service.PhoneFeeQueryService;
import com.anbang.qipai.members.msg.service.PrizeLogMsgService;
import com.anbang.qipai.members.plan.bean.Address;
import com.anbang.qipai.members.web.vo.CommonVO;
import com.anbang.qipai.members.web.vo.LotteryVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/lottery")
public class RaffleController {

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


    @ResponseBody
    @RequestMapping("/list")
    public CommonVO list(String token) {final String memberId = this.memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            return new CommonVO(false, "用户未登陆", null);
        }
        final List<LotteryDbo> lotteryDboList = this.lotteryQueryService.findAll();
        List<LotteryVo> lotteryVoList = new ArrayList<>();
        for (LotteryDbo lotteryDbo : lotteryDboList) {
            LotteryVo lotteryVo = new LotteryVo(lotteryDbo.getId(), lotteryDbo.getIcon(), lotteryDbo.getName());
            lotteryVoList.add(lotteryVo);
        }
        return new CommonVO(true, null, lotteryVoList);
    }

    @ResponseBody
    @RequestMapping("/set_address")
    public CommonVO setLotteryAddress(String token, String id, Address address) {
        final String memberId = this.memberAuthService.getMemberIdBySessionId(token);
        if (memberId == null) {
            return new CommonVO(false, "用户未登陆", null);
        }
        if (StringUtils.isEmpty(address.getAddress()) || StringUtils.isEmpty(address.getPhone()) || StringUtils.isEmpty(address.getName())) {
            return new CommonVO(false, "地址信息不完整", null);
        }
        MemberRaffleHistoryDbo raffleHistoryDbo = this.memberRaffleQueryService.setRaffleAddress(id, memberId, address);
        this.prizeLogMsgService.sendRaffleRecord(raffleHistoryDbo);
        if (raffleHistoryDbo != null) {
            return new CommonVO(true, null, null);
        } else {
            return new CommonVO(false, "奖励不存在", null);
        }
    }


}
