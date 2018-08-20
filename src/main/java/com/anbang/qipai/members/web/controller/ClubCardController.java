package com.anbang.qipai.members.web.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.cqrs.c.service.MemberGoldCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberScoreCmdService;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldRecordDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreRecordDbo;
import com.anbang.qipai.members.cqrs.q.service.MemberGoldQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberScoreQueryService;
import com.anbang.qipai.members.msg.service.GoldsMsgService;
import com.anbang.qipai.members.msg.service.MemberClubCardsMsgService;
import com.anbang.qipai.members.msg.service.MemberOrdersMsgService;
import com.anbang.qipai.members.msg.service.MembersMsgService;
import com.anbang.qipai.members.msg.service.ScoresMsgService;
import com.anbang.qipai.members.plan.bean.MemberClubCard;
import com.anbang.qipai.members.plan.bean.MemberOrder;
import com.anbang.qipai.members.plan.service.AlipayService;
import com.anbang.qipai.members.plan.service.ClubCardService;
import com.anbang.qipai.members.plan.service.MemberOrderService;
import com.anbang.qipai.members.plan.service.MemberService;
import com.anbang.qipai.members.plan.service.WXpayService;
import com.anbang.qipai.members.util.IPUtil;
import com.anbang.qipai.members.web.vo.CommonVO;
import com.dml.accounting.AccountingRecord;

/**
 * 会员卡controller
 * 
 * @author 林少聪 2018.7.9
 *
 */
@RestController
@RequestMapping("/clubcard")
public class ClubCardController {

	@Autowired
	private MemberAuthService memberAuthService;

	@Autowired
	private ClubCardService clubCardService;

	@Autowired
	private MemberOrderService memberOrderService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberScoreCmdService memberScoreCmdService;

	@Autowired
	private MemberScoreQueryService memberScoreQueryService;

	@Autowired
	private ScoresMsgService scoresMsgService;

	@Autowired
	private MembersMsgService membersMsgService;

	@Autowired
	private MemberGoldCmdService memberGoldCmdService;

	@Autowired
	private MemberGoldQueryService memberGoldQueryService;

	@Autowired
	private GoldsMsgService goldsMsgService;

	@Autowired
	private MemberOrdersMsgService ordersMsgService;

	@Autowired
	private MemberClubCardsMsgService memberClubCardsMsgService;

	@Autowired
	private WXpayService wxpayService;

	@Autowired
	private AlipayService alipayService;

	@RequestMapping("/addclubcard")
	public CommonVO addClubCard(@RequestBody MemberClubCard clubCard) {
		CommonVO vo = new CommonVO();
		clubCardService.addClubCard(clubCard);
		memberClubCardsMsgService.addClubCard(clubCard);
		return vo;
	}

	@RequestMapping("/deleteclubcards")
	public CommonVO deleteClubCards(@RequestBody String[] clubCardIds) {
		CommonVO vo = new CommonVO();
		clubCardService.deleteClubCards(clubCardIds);
		memberClubCardsMsgService.deleteClubCards(clubCardIds);
		return vo;
	}

	@RequestMapping("/updateclubcard")
	public CommonVO updateClubCards(@RequestBody MemberClubCard clubCard) {
		CommonVO vo = new CommonVO();
		clubCardService.updateClubCard(clubCard);
		memberClubCardsMsgService.updateClubCards(clubCard);
		return vo;
	}

	/**
	 * 生成支付宝订单
	 * 
	 * @param memberId
	 *            购买人
	 * @param clubCardId
	 *            购买的会员卡
	 * @return 订单信息
	 */
	@RequestMapping("/createalipayorder")
	public CommonVO createAliPayOrder(String token, String clubCardId, HttpServletRequest request) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		MemberClubCard card = clubCardService.findClubCardById(clubCardId);
		MemberDbo member = memberService.findMemberById(memberId);
		// 获取真实ip
		String reqIP = IPUtil.getRealIp(request);
		// 保存订单
		MemberOrder order = memberOrderService.addMemberOrder(member.getId(), member.getNickname(), member.getId(),
				member.getNickname(), card.getId(), card.getName(), card.getPrice(), card.getGold(), card.getScore(),
				card.getTime(), 1, "支付宝支付", reqIP);
		ordersMsgService.createOrder(order);
		String orderString = null;
		// 下订单
		try {
			orderString = alipayService.getOrderInfo(order);
			vo.setSuccess(true);
			vo.setMsg("sign orderInfo");
			vo.setData(orderString);
		} catch (UnsupportedEncodingException e) {
			vo.setSuccess(false);
			vo.setMsg("UnsupportedEncodingException");
		}
		return vo;
	}

	@RequestMapping("/alipaynotify")
	public String alipayNotify(HttpServletRequest request) {
		Map<String, String> paramMap = alipayService.alipayNotify(request);
		if (paramMap == null) {
			return "fail";
		}
		String id = paramMap.get("out_trade_no");
		String transaction_id = paramMap.get("trade_no");
		String status = paramMap.get("trade_status");

		MemberOrder order = memberOrderService.findMemberOrderById(id);
		if ("TRADE_SUCCESS".equals(order.getStatus()) || "TRADE_FINISHED".equals(order.getStatus())
				|| "TRADE_CLOSED".equals(order.getStatus())) {
			return "success";
		}
		MemberOrder finishedOrder = memberOrderService.orderFinished(id, transaction_id, status,
				System.currentTimeMillis());
		// kafka发消息
		ordersMsgService.orderFinished(finishedOrder);
		// 交易成功
		if ("TRADE_SUCCESS".equals(status)) {

			memberService.updateVIP(order);
			// 发送会员信息
			membersMsgService.updateMemberVip(memberService.findMemberById(order.getReceiverId()));
			try {
				AccountingRecord goldrcd = memberGoldCmdService.giveGoldToMember(order.getReceiverId(), order.getGold(),
						"give for buy clubcard", System.currentTimeMillis());
				AccountingRecord scorercd = memberScoreCmdService.giveScoreToMember(order.getReceiverId(),
						order.getScore(), "give for buy clubcard", System.currentTimeMillis());
				MemberGoldRecordDbo golddbo = memberGoldQueryService.withdraw(order.getReceiverId(), goldrcd);
				MemberScoreRecordDbo scoredbo = memberScoreQueryService.withdraw(order.getReceiverId(), scorercd);
				// TODO: rcd发kafka
				goldsMsgService.withdraw(golddbo);
				scoresMsgService.withdraw(scoredbo);
			} catch (MemberNotFoundException e) {
				e.printStackTrace();
			}
		}
		return "success";
	}

	@RequestMapping("/alipayquery")
	public CommonVO alipayQuery(String result) {
		CommonVO vo = new CommonVO();
		Map<String, String> params = alipayService.alipayQuery(result);
		if (params == null) {
			vo.setSuccess(false);
			vo.setMsg("query fail");
			return vo;
		}
		String id = params.get("out_trade_no");
		String transaction_id = params.get("trade_no");
		String status = params.get("trade_status");

		// 当客户端连续发出多次请求，可能出现并发问题
		MemberOrder order = memberOrderService.findMemberOrderById(id);
		Map data = new HashMap<>();
		data.put("gold", order.getGold());
		data.put("score", order.getScore());
		data.put("productName", order.getProductName());
		data.put("number", order.getNumber());
		if ("TRADE_SUCCESS".equals(order.getStatus()) || "TRADE_FINISHED".equals(order.getStatus())
				|| "TRADE_CLOSED".equals(order.getStatus())) {
			vo.setSuccess(true);
			vo.setData(data);
			return vo;
		}
		MemberOrder finishedOrder = memberOrderService.orderFinished(id, transaction_id, status,
				System.currentTimeMillis());
		// kafka发消息
		ordersMsgService.orderFinished(finishedOrder);
		// 交易成功
		if ("TRADE_SUCCESS".equals(status)) {

			memberService.updateVIP(order);
			// 发送会员信息
			membersMsgService.updateMemberVip(memberService.findMemberById(order.getReceiverId()));
			try {
				AccountingRecord goldrcd = memberGoldCmdService.giveGoldToMember(order.getReceiverId(), order.getGold(),
						"give for buy clubcard", System.currentTimeMillis());
				AccountingRecord scorercd = memberScoreCmdService.giveScoreToMember(order.getReceiverId(),
						order.getScore(), "give for buy clubcard", System.currentTimeMillis());
				MemberGoldRecordDbo golddbo = memberGoldQueryService.withdraw(order.getReceiverId(), goldrcd);
				MemberScoreRecordDbo scoredbo = memberScoreQueryService.withdraw(order.getReceiverId(), scorercd);
				// TODO: rcd发kafka
				goldsMsgService.withdraw(golddbo);
				scoresMsgService.withdraw(scoredbo);
			} catch (MemberNotFoundException e) {
				vo.setMsg("MemberNotFoundException");
			}
		}
		vo.setSuccess(true);
		vo.setData(data);
		return vo;
	}

	@RequestMapping("/createwxorder")
	public CommonVO createWXOrder(String token, String clubCardId, HttpServletRequest request) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		MemberClubCard card = clubCardService.findClubCardById(clubCardId);
		MemberDbo member = memberService.findMemberById(memberId);

		String reqIP = IPUtil.getRealIp(request);

		MemberOrder order = memberOrderService.addMemberOrder(member.getId(), member.getNickname(), member.getId(),
				member.getNickname(), card.getId(), card.getName(), card.getPrice(), card.getGold(), card.getScore(),
				card.getTime(), 1, "微信支付", reqIP);
		ordersMsgService.createOrder(order);
		try {
			Map<String, String> resultMap = wxpayService.createOrder_APP(order);
			if (resultMap == null) {
				vo.setSuccess(false);
				vo.setMsg("order fail");
			} else {
				vo.setSuccess(true);
				vo.setMsg("orderinfo");
				vo.setData(resultMap);
			}
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
		}
		return vo;
	}

	@RequestMapping("/createwxorder_h5")
	public CommonVO createWXOrder_H5(String token, String clubCardId, HttpServletRequest request) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		MemberClubCard card = clubCardService.findClubCardById(clubCardId);
		MemberDbo member = memberService.findMemberById(memberId);

		String reqIP = IPUtil.getRealIp(request);

		MemberOrder order = memberOrderService.addMemberOrder(member.getId(), member.getNickname(), member.getId(),
				member.getNickname(), card.getId(), card.getName(), card.getPrice(), card.getGold(), card.getScore(),
				card.getTime(), 1, "微信支付", reqIP);
		ordersMsgService.createOrder(order);
		try {
			String mweb_url = wxpayService.createOrder_H5(order);
			if (mweb_url == null) {
				vo.setSuccess(false);
				vo.setMsg("order fail");
			} else {
				vo.setSuccess(true);
				vo.setMsg("mweb_url");
				vo.setData(mweb_url);
			}
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
		}
		return vo;
	}

	@RequestMapping("/wxnotify")
	public String wxNotify(HttpServletRequest request) {
		SortedMap<String, String> resultMap = null;
		try {
			resultMap = wxpayService.receiveNotify(request);
		} catch (IOException e) {
			return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[验签失败]]></return_msg></xml>";
		}
		if (resultMap != null) {
			String transaction_id = resultMap.get("transaction_id");
			SortedMap<String, String> responseMap = null;
			try {
				responseMap = wxpayService.queryOrderResult(transaction_id, null);
			} catch (Exception e) {
				return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[参数格式校验错误]]></return_msg></xml>";
			}
			if (responseMap != null && "SUCCESS".equals(responseMap.get("result_code"))) {
				String out_trade_no = responseMap.get("out_trade_no");
				String trade_state = responseMap.get("trade_state");
				MemberOrder order = memberOrderService.findMemberOrderById(out_trade_no);
				if ("SUCCESS".equals(order.getStatus()) || "CLOSED".equals(order.getStatus())
						|| "PAYERROR".equals(order.getStatus())) {
					return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
				}
				MemberOrder finishedOrder = memberOrderService.orderFinished(out_trade_no, transaction_id, trade_state,
						System.currentTimeMillis());
				// kafka发消息
				ordersMsgService.orderFinished(finishedOrder);

				// 交易成功
				if ("SUCCESS".equals(trade_state)) {
					memberService.updateVIP(order);
					// 发送会员信息
					membersMsgService.updateMemberVip(memberService.findMemberById(order.getReceiverId()));
					try {
						AccountingRecord goldrcd = memberGoldCmdService.giveGoldToMember(order.getReceiverId(),
								order.getGold(), "give for buy clubcard", System.currentTimeMillis());
						AccountingRecord scorercd = memberScoreCmdService.giveScoreToMember(order.getReceiverId(),
								order.getScore(), "give for buy clubcard", System.currentTimeMillis());
						MemberGoldRecordDbo golddbo = memberGoldQueryService.withdraw(order.getReceiverId(), goldrcd);
						MemberScoreRecordDbo scoredbo = memberScoreQueryService.withdraw(order.getReceiverId(),
								scorercd);
						// TODO: rcd发kafka
						goldsMsgService.withdraw(golddbo);
						scoresMsgService.withdraw(scoredbo);
					} catch (MemberNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[验签失败]]></return_msg></xml>";
	}

	@RequestMapping("/wxquery")
	public CommonVO wxQuery(String out_trade_no) {
		CommonVO vo = new CommonVO();
		SortedMap<String, String> responseMap = null;
		try {
			responseMap = wxpayService.queryOrderResult(null, out_trade_no);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		if (responseMap != null && "SUCCESS".equals(responseMap.get("result_code"))) {
			String transaction_id = responseMap.get("transaction_id");
			String trade_state = responseMap.get("trade_state");

			// 客户端连续发出多次请求，可能出现并发问题
			MemberOrder order = memberOrderService.findMemberOrderById(out_trade_no);
			Map data = new HashMap<>();
			data.put("gold", order.getGold());
			data.put("score", order.getScore());
			data.put("productName", order.getProductName());
			data.put("number", order.getNumber());
			if ("SUCCESS".equals(order.getStatus()) || "CLOSED".equals(order.getStatus())
					|| "PAYERROR".equals(order.getStatus())) {
				vo.setSuccess(true);
				vo.setData(data);
				return vo;
			}
			// 交易成功
			if ("SUCCESS".equals(trade_state)) {
				MemberOrder finishedOrder = memberOrderService.orderFinished(out_trade_no, transaction_id, trade_state,
						System.currentTimeMillis());
				// kafka发消息
				ordersMsgService.orderFinished(finishedOrder);

				memberService.updateVIP(order);
				// 发送会员信息
				membersMsgService.updateMemberVip(memberService.findMemberById(order.getReceiverId()));
				try {
					AccountingRecord goldrcd = memberGoldCmdService.giveGoldToMember(order.getReceiverId(),
							order.getGold(), "give for buy clubcard", System.currentTimeMillis());
					AccountingRecord scorercd = memberScoreCmdService.giveScoreToMember(order.getReceiverId(),
							order.getScore(), "give for buy clubcard", System.currentTimeMillis());
					MemberGoldRecordDbo golddbo = memberGoldQueryService.withdraw(order.getReceiverId(), goldrcd);
					MemberScoreRecordDbo scoredbo = memberScoreQueryService.withdraw(order.getReceiverId(), scorercd);
					// TODO: rcd发kafka
					goldsMsgService.withdraw(golddbo);
					scoresMsgService.withdraw(scoredbo);
				} catch (MemberNotFoundException e) {
					vo.setMsg("MemberNotFoundException");
				}
			}
			vo.setSuccess(true);
			vo.setData(data);
			return vo;
		}
		vo.setSuccess(false);
		vo.setMsg("query fail");
		return vo;
	}

}
