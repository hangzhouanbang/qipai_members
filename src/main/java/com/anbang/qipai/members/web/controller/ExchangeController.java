package com.anbang.qipai.members.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.domain.prize.ExchangeException;
import com.anbang.qipai.members.cqrs.c.domain.prize.ExchangeRecord;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.cqrs.c.service.MemberHongBaoCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberPhoneFeeCmdService;
import com.anbang.qipai.members.cqrs.q.dbo.HongBaoAccountDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberHongBaoRecordDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberRaffleHistoryDbo;
import com.anbang.qipai.members.cqrs.q.dbo.PhoneFeeAccountDbo;
import com.anbang.qipai.members.cqrs.q.dbo.PhoneFeeRecordDbo;
import com.anbang.qipai.members.cqrs.q.dbo.ReceiverInfoDbo;
import com.anbang.qipai.members.cqrs.q.service.HongBaoQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberRaffleQueryService;
import com.anbang.qipai.members.cqrs.q.service.PhoneFeeQueryService;
import com.anbang.qipai.members.cqrs.q.service.ReceiverInfoQueryService;
import com.anbang.qipai.members.enums.ExchangeType;
import com.anbang.qipai.members.msg.service.PrizeLogMsgService;
import com.anbang.qipai.members.msg.service.ReceiverInfoMsgService;
import com.anbang.qipai.members.web.vo.CommonVO;
import com.anbang.qipai.members.web.vo.EntityExchangeDO;
import com.anbang.qipai.members.web.vo.ExchangeFeeVO;
import com.anbang.qipai.members.web.vo.RaffleHistoryVO;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;

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

	@Autowired
	private ReceiverInfoMsgService receiverInfoMsgService;

	@RequestMapping("/hongbao")
	@ResponseBody
	public CommonVO exchangeHongBao(String token, int score, String phone) {
		final String memberId = this.memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			return new CommonVO(false, "用户未登陆", null);
		}
		try {
			ExchangeRecord exchangeRecord = this.memberHongBaoCmdService.exchange(memberId, score, "红包兑换 * " + score,
					System.currentTimeMillis());
			AccountingRecord accountingRecord = exchangeRecord.getRecord();
			MemberHongBaoRecordDbo hongBaoRecordDbo = this.hongBaoQueryService.withdraw(memberId, accountingRecord);

			// ScoreExchangeRecordDbo scoreExchangeRecordDbo = new
			// ScoreExchangeRecordDbo(hongBaoRecordDbo.getId(),
			// memberId,
			// null,
			// hongBaoRecordDbo.getTime(),
			// (int) hongBaoRecordDbo.getAccountingAmount(),
			// exchangeRecord.getConcurrency(),
			// ExchangeType.HONG_BAO,
			// (int) hongBaoRecordDbo.getBalanceAfter(),
			// accountingRecord);
			//
			EntityExchangeDO entityExchangeDO = new EntityExchangeDO();
			entityExchangeDO.setMemberId(memberId);
			entityExchangeDO.setNickName(memberAuthQueryService.findNameByMemberID(memberId));
			entityExchangeDO.setLotteryName("红包");
			entityExchangeDO.setLotteryType(ExchangeType.HONG_BAO.name());
			entityExchangeDO.setSingleNum(String.valueOf(exchangeRecord.getConcurrency()));
			entityExchangeDO.setTelephone(phone);
			entityExchangeDO.setRest(String.valueOf(hongBaoRecordDbo.getBalanceAfter()));
			entityExchangeDO.setExchangeTime(hongBaoRecordDbo.getTime());

			prizeLogMsgService.sendEntityExchangeLog(entityExchangeDO);

			// this.prizeLogMsgService.sendExchangeLog(scoreExchangeRecordDbo);

			// 给U3D看的
			ExchangeFeeVO exchangeFeeVO = new ExchangeFeeVO();
			exchangeFeeVO.setPhone(phone);
			exchangeFeeVO.setCurrency(exchangeRecord.getConcurrency());
			exchangeFeeVO.setMemberId(memberId);
			exchangeFeeVO.setExchange(ExchangeType.HONG_BAO.name());

			return new CommonVO(true, "成功兑换红包 " + exchangeFeeVO.getCurrency() + " 元  \n 将在48小时内充值到账", exchangeFeeVO);
		} catch (InsufficientBalanceException e) {
			return new CommonVO(false, "红包积分不足", null);
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
		if (StringUtils.isEmpty(phone)) {
			return new CommonVO(false, "手机号不得为空", null);
		}
		// 手机号11位
		if (phone.length() != 11) {
			return new CommonVO(false, "手机号格式不正确", null);
		}
		try {
			ExchangeRecord exchangeRecord = this.memberPhoneFeeCmdService.exchange(memberId, score, "用户话费兑换 *" + score,
					System.currentTimeMillis());
			AccountingRecord accountingRecord = exchangeRecord.getRecord();
			PhoneFeeRecordDbo phoneFeeRecordDbo = phoneFeeQueryService.withdraw(memberId, accountingRecord);

			// ScoreExchangeRecordDbo scoreExchangeRecordDbo = new
			// ScoreExchangeRecordDbo(phoneFeeRecordDbo.getId(),
			// memberId,
			// phone,
			// phoneFeeRecordDbo.getTime(),
			// (int) phoneFeeRecordDbo.getAccountingAmount(),
			// exchangeRecord.getConcurrency(),
			// ExchangeType.PHONE_FEE,
			// (int) phoneFeeRecordDbo.getBalanceAfter(), accountingRecord);

			EntityExchangeDO entityExchangeDO = new EntityExchangeDO();
			entityExchangeDO.setMemberId(memberId);
			entityExchangeDO.setNickName(memberAuthQueryService.findNameByMemberID(memberId));
			entityExchangeDO.setLotteryName("话费");
			entityExchangeDO.setLotteryType(ExchangeType.PHONE_FEE.name());
			entityExchangeDO.setSingleNum(String.valueOf(exchangeRecord.getConcurrency()));
			entityExchangeDO.setTelephone(phone);
			entityExchangeDO.setExchangeTime(phoneFeeRecordDbo.getTime());
			entityExchangeDO.setRest(String.valueOf(phoneFeeRecordDbo.getBalanceAfter()));

			prizeLogMsgService.sendEntityExchangeLog(entityExchangeDO);
			// this.prizeLogMsgService.sendExchangeLog(scoreExchangeRecordDbo);

			// 展示给U3D的
			ExchangeFeeVO exchangeFeeVO = new ExchangeFeeVO();
			exchangeFeeVO.setCurrency(exchangeRecord.getConcurrency());
			exchangeFeeVO.setMemberId(memberId);
			exchangeFeeVO.setPhone(phone);
			exchangeFeeVO.setExchange(ExchangeType.PHONE_FEE.name());

			return new CommonVO(true, "成功兑换话费 " + exchangeFeeVO.getCurrency() + " 元   \n 将在48小时内充值到账", exchangeFeeVO);
		} catch (InsufficientBalanceException e) {
			return new CommonVO(false, "话费余额不足", null);
		} catch (MemberNotFoundException e) {
			return new CommonVO(false, "账户不存在", null);
		} catch (ExchangeException e) {
			return new CommonVO(false, e.getMessage(), null);
		}
	}

	@RequestMapping(value = { "/memberrafflehistory" })
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

			// 如果是实体的话 需要判断有没有兑换过
			if (memberRaffleHistoryDbo.getLottery().getType().name().equals("ENTIRY")) {

				// 如果该字段没有记录的话 说明没有兑换过
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

	@RequestMapping(value = { "/addinformation" })
	@ResponseBody
	public CommonVO addInformation(String token, ReceiverInfoDbo receiverInfoDbo) {
		CommonVO vo = new CommonVO();

		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}

		ReceiverInfoDbo receiverInfo = receiverInfoQueryService.findReceiverByMemberId(memberId);
		if (receiverInfo == null) {
			if (StringUtils.isEmpty(receiverInfoDbo.getName()) || StringUtils.isEmpty(receiverInfoDbo.getAddress())
					|| StringUtils.isEmpty(receiverInfoDbo.getTelephone())) {
				vo.setSuccess(false);
				vo.setMsg("手机号、收件地址、收货人不得为空");
				return vo;
			}

			// 手机号11位
			if (receiverInfoDbo.getTelephone().length() != 11) {
				vo.setSuccess(false);
				vo.setMsg("手机号格式不正确");
			}

			receiverInfo = new ReceiverInfoDbo();
			receiverInfo.setId(memberId + "_receiverInfo");
			receiverInfo.setAddress(receiverInfoDbo.getAddress());
			receiverInfo.setGender(receiverInfoDbo.getGender());
			receiverInfo.setName(receiverInfoDbo.getName());
			receiverInfo.setTelephone(receiverInfoDbo.getTelephone());
			receiverInfo.setMemberId(memberId);
			receiverInfoQueryService.addReceiverInfo(receiverInfo);
			receiverInfoMsgService.recordReceiverInfo(receiverInfo);
		} else {
			if (!StringUtils.isEmpty(receiverInfoDbo.getTelephone())) {
				// 手机号11位
				if (receiverInfoDbo.getTelephone().length() != 11) {
					vo.setSuccess(false);
					vo.setMsg("手机号格式不正确");
				}

				receiverInfo.setTelephone(receiverInfoDbo.getTelephone());
			}
			receiverInfo.setName(receiverInfoDbo.getName());
			receiverInfo.setGender(receiverInfoDbo.getGender());
			receiverInfo.setAddress(receiverInfoDbo.getAddress());
			receiverInfoQueryService.save(receiverInfo);
			receiverInfoMsgService.updateReceiverInfo(receiverInfo);
		}
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

		if (!StringUtils.isEmpty(raffleHistoryDbo.getHasExchange())) {
			if (!raffleHistoryDbo.getHasExchange().equals("NO")) {
				return new CommonVO(false, "您已经兑换过了", null);
			}
		}

		if (raffleHistoryDbo.getLottery().getType().name().equals("PHONE_FEE")) {
			if (StringUtils.isEmpty(receiverInfo.getTelephone())) {
				return new CommonVO(false, "手机号码不得为空", null);
			}
		} else {
			if (StringUtils.isEmpty(receiverInfo.getTelephone()) || StringUtils.isEmpty(receiverInfo.getAddress())
					|| StringUtils.isEmpty(receiverInfo.getName())) {
				return new CommonVO(false, "收件人信息不得为空", null);
			}
		}

		EntityExchangeDO entityExchangeDO = new EntityExchangeDO();
		// String nickName = memberAuthQueryService.findNameByMemberID(memberId);

		// entityId为中奖记录的ID
		entityExchangeDO.setRaffleRecordId(entityId);
		entityExchangeDO.setMemberId(memberId);
		entityExchangeDO.setNickName(receiverInfo.getName());
		entityExchangeDO.setTelephone(receiverInfo.getTelephone());
		entityExchangeDO.setAddress(receiverInfo.getAddress());
		entityExchangeDO.setExchangeTime(System.currentTimeMillis());
		entityExchangeDO.setLotteryName(raffleHistoryDbo.getLottery().getName());
		entityExchangeDO.setLotteryType(raffleHistoryDbo.getLottery().getType().name());
		entityExchangeDO.setSingleNum(String.valueOf(raffleHistoryDbo.getLottery().getSingleNum()) + "个");

		raffleHistoryDbo.setHasExchange("WAIT");
		memberRaffleQueryService.save(raffleHistoryDbo);
		// TODO:发送消息
		this.prizeLogMsgService.sendEntityExchangeLog(entityExchangeDO);

		return new CommonVO(true, "成功兑换 " + entityExchangeDO.getLotteryName() + " \n  \n询问详情请联系客服微信ankf01",
				entityExchangeDO);
	}

	@RequestMapping(value = { "/queryphonefeeAndHongbao" })
	@ResponseBody
	public CommonVO queryPhoneFee(String token) {
		final String memberId = this.memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			return new CommonVO(false, "用户未登陆", null);
		}

		PhoneFeeAccountDbo phoneFeeAccountDbo = phoneFeeQueryService.findAccount(memberId);

		double phoneFee = 0;
		if (phoneFeeAccountDbo != null) {
			phoneFee = phoneFeeAccountDbo.getBalance();
		}

		double hongBao = 0;
		HongBaoAccountDbo hongBaoAccountDbo = hongBaoQueryService.findAccount(memberId);
		hongBaoQueryService.findAccount(memberId);
		if (hongBaoAccountDbo != null) {
			hongBao = hongBaoAccountDbo.getBalance();
		}

		Map<String, String> resultMap = new HashMap<>();

		resultMap.put("phone", String.valueOf(phoneFee));
		resultMap.put("hongbao", String.valueOf(hongBao));

		CommonVO commonVO = new CommonVO();
		commonVO.setSuccess(true);
		commonVO.setData(resultMap);
		return commonVO;
	}

	@RequestMapping(value = { "/queryreceiverinfo" })
	@ResponseBody
	public CommonVO queryReceiverInfo(String token) {
		final String memberId = this.memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			return new CommonVO(false, "用户未登陆", null);
		}
		ReceiverInfoDbo receiver = receiverInfoQueryService.findReceiverByMemberId(memberId);
		if (receiver == null) {
			return new CommonVO(false, "请先填写收货人信息", null);
		}
		CommonVO vo = new CommonVO();
		vo.setSuccess(true);
		vo.setData(receiver);
		return vo;
	}

	@RequestMapping(value = { "/updatetelephone" })
	@ResponseBody
	public CommonVO updateTelephone(String token, String telephone) {
		final String memberId = this.memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			return new CommonVO(false, "用户未登陆", null);
		}

		// 手机号11位
		if (telephone.length() != 11) {
			return new CommonVO(false, "手机号格式不正确", null);
		}

		ReceiverInfoDbo receiverInfo = receiverInfoQueryService.findReceiverByMemberId(memberId);
		if (receiverInfo == null) {
			receiverInfo = new ReceiverInfoDbo();
			receiverInfo.setId(memberId + "_receiverInfo");
			receiverInfo.setTelephone(telephone);
			receiverInfo.setMemberId(memberId);
			receiverInfoQueryService.addReceiverInfo(receiverInfo);
		} else {
			receiverInfo.setTelephone(telephone);
			receiverInfoQueryService.save(receiverInfo);
		}
		return new CommonVO(true, "修改手机号成功", null);
	}

	@RequestMapping(value = { "/querytelephone" })
	@ResponseBody
	public CommonVO queryTelephone(String token) {
		final String memberId = this.memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			return new CommonVO(false, "用户未登陆", null);
		}
		CommonVO vo = new CommonVO();
		vo.setSuccess(true);
		ReceiverInfoDbo receiver = receiverInfoQueryService.findReceiverByMemberId(memberId);

		if (receiver == null) {
			return vo;
		} else {
			vo.setData(receiver.getTelephone());
			return vo;
		}
	}
}
