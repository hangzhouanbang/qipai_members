package com.anbang.qipai.members.web.controller;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.service.MemberGoldCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberScoreCmdService;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldRecordDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreRecordDbo;
import com.anbang.qipai.members.cqrs.q.service.MemberGoldQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberScoreQueryService;
import com.anbang.qipai.members.msg.service.GoldsMsgService;
import com.anbang.qipai.members.msg.service.MembersMsgService;
import com.anbang.qipai.members.msg.service.OrdersMsgService;
import com.anbang.qipai.members.msg.service.ScoresMsgService;
import com.anbang.qipai.members.plan.domain.ClubCard;
import com.anbang.qipai.members.plan.domain.Order;
import com.anbang.qipai.members.plan.domain.RefundOrder;
import com.anbang.qipai.members.plan.service.AlipayService;
import com.anbang.qipai.members.plan.service.ClubCardService;
import com.anbang.qipai.members.plan.service.MemberService;
import com.anbang.qipai.members.plan.service.OrderService;
import com.anbang.qipai.members.plan.service.RefundOrderService;
import com.anbang.qipai.members.plan.service.WXpayService;
import com.anbang.qipai.members.web.vo.CommonVO;
import com.dml.accounting.AccountingRecord;

@RestController
@RequestMapping("/clubcard")
public class ClubCardController {

	@Autowired
	private ClubCardService clubCardService;

	@Autowired
	private OrderService orderService;

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
	private RefundOrderService refundOrderService;
	@Autowired
	private OrdersMsgService ordersMsgService;
	@Autowired
	private WXpayService wxpayService;

	@Autowired
	private AlipayService alipayService;

	@RequestMapping("/showclubcard")
	public CommonVO showClubCards() {
		CommonVO vo = new CommonVO();
		List<ClubCard> cardList = clubCardService.showClubCard();
		vo.setSuccess(true);
		vo.setMsg("clubCardList");
		vo.setData(cardList);
		return vo;
	}

	@RequestMapping("/addclubcard")
	public CommonVO addClubCard(@RequestBody ClubCard clubCard) {
		CommonVO vo = new CommonVO();
		clubCardService.addClubCard(clubCard);
		vo.setSuccess(true);
		vo.setMsg("member add success");
		vo.setData(clubCard);
		return vo;
	}

	@RequestMapping("/deleteclubcards")
	public CommonVO deleteClubCards(@RequestBody String[] clubCardIds) {
		CommonVO vo = new CommonVO();
		if (clubCardService.deleteClubCards(clubCardIds)) {
			vo.setSuccess(true);
			vo.setMsg("member delete success");
			vo.setData(clubCardIds);
		} else {
			vo.setSuccess(false);
			vo.setMsg("member delete fail");
		}
		return vo;
	}

	@RequestMapping("/updateclubcard")
	public CommonVO updateClubCards(@RequestBody ClubCard clubCard) {
		CommonVO vo = new CommonVO();
		if (clubCardService.updateClubCard(clubCard)) {
			vo.setSuccess(true);
			vo.setMsg("member update success");
			vo.setData(clubCard);
		} else {
			vo.setSuccess(false);
			vo.setMsg("member update fail");
		}
		return vo;
	}

	/**
	 * 生成支付宝订单
	 * 
	 * @param memberId
	 *            购买人
	 * @param clubCardId
	 *            购买的会员卡
	 * @param number
	 *            数量
	 * @return 订单信息
	 */
	@RequestMapping("/createalipayorder")
	public CommonVO createAliPayOrder(String memberId, String clubCardId,
			@RequestParam(defaultValue = "1") Integer number, HttpServletRequest request) {
		CommonVO vo = new CommonVO();
		Order order = orderService.addOrder(memberId, clubCardId, number, "alipay", request.getRemoteAddr());
		// kafka发消息
		ordersMsgService.createOrder(order);
		String orderString = alipayService.getOrderInfo(order);
		vo.setSuccess(true);
		vo.setMsg("sign orderInfo");
		vo.setData(orderString);
		return vo;
	}

	@RequestMapping("/checkalipay")
	public CommonVO checkAlipay(String result) {
		CommonVO vo = new CommonVO();
		System.out.println(result);
		vo.setSuccess(true);
		vo.setMsg("success");
		return vo;
	}

	@RequestMapping("/alipaynotify")
	public String alipayNotify(HttpServletRequest request) {
		Order order = alipayService.alipayNotify(request);
		if (order == null) {
			return "fail";
		}
		if (order.getStatus() == -1) {
			ordersMsgService.updateOrder(order);
		}
		if (order.getStatus() == 0) {
			memberService.updateVIP(order);
			// 发送会员信息
			membersMsgService.updateMember(memberService.findMemberById(order.getMemberId()));
			try {
				AccountingRecord goldrcd = memberGoldCmdService.giveGoldToMember(order.getMemberId(),
						order.getGold() * order.getNumber(), "give for buy clubcard", System.currentTimeMillis());
				AccountingRecord scorercd = memberScoreCmdService.giveScoreToMember(order.getMemberId(),
						order.getScore() * order.getNumber(), "give for buy clubcard", System.currentTimeMillis());
				MemberGoldRecordDbo golddbo = memberGoldQueryService.withdraw(order.getMemberId(), goldrcd);
				MemberScoreRecordDbo scoredbo = memberScoreQueryService.withdraw(order.getMemberId(), scorercd);
				// TODO: rcd发kafka
				goldsMsgService.withdraw(golddbo);
				scoresMsgService.withdraw(scoredbo);
				orderService.updateDeliveTime(order.getOut_trade_no(), System.currentTimeMillis());
				orderService.updateOrderStatus(order.getOut_trade_no(), 1);
				// kafka发消息
				order = orderService.findOrderByOut_trade_no(order.getOut_trade_no());
				ordersMsgService.updateOrder(order);
			} catch (MemberNotFoundException e) {
				e.printStackTrace();
			}
		}
		return "success";
	}

	@RequestMapping("/createwxorder")
	public CommonVO createWXOrder(String memberId, String clubCardId, @RequestParam(defaultValue = "1") Integer number,
			HttpServletRequest request) {
		CommonVO vo = new CommonVO();
		Order order = orderService.addOrder(memberId, clubCardId, number, "wxpay", request.getRemoteAddr());
		// kafka发消息
		ordersMsgService.createOrder(order);
		try {
			Map<String, String> resultMap = wxpayService.createOrder(order, request.getRemoteAddr());
			vo.setSuccess(true);
			vo.setMsg("success");
			vo.setData(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			vo.setSuccess(false);
			vo.setMsg("fail");
		}
		return vo;
	}

	@RequestMapping("/receivewxnotify")
	public String receiveWXNotify(HttpServletRequest request) {
		return wxpayService.receiveNotify(request);
	}

	@RequestMapping("/querywxorderresult")
	public CommonVO queryWXOrderResult(String transaction_id) {
		CommonVO vo = new CommonVO();
		try {
			SortedMap<String, String> responseMap = wxpayService.queryOrderResult(transaction_id);
			if ("SUCCESS".equals(responseMap.get("result_code"))) {
				Order order = orderService.findOrderByOut_trade_no(responseMap.get("out_trade_no"));
				if ("SUCCESS".equals(responseMap.get("trade_state")) && order.getStatus() == 0) {
					memberService.updateVIP(order);
					// 发送会员信息
					membersMsgService.updateMember(memberService.findMemberById(order.getMemberId()));
					try {
						AccountingRecord goldrcd = memberGoldCmdService.giveGoldToMember(order.getMemberId(),
								order.getGold() * order.getNumber(), "give for buy clubcard",
								System.currentTimeMillis());
						AccountingRecord scorercd = memberScoreCmdService.giveScoreToMember(order.getMemberId(),
								order.getScore() * order.getNumber(), "give for buy clubcard",
								System.currentTimeMillis());
						MemberGoldRecordDbo golddbo = memberGoldQueryService.withdraw(order.getMemberId(), goldrcd);
						MemberScoreRecordDbo scoredbo = memberScoreQueryService.withdraw(order.getMemberId(), scorercd);
						// TODO: rcd发kafka
						goldsMsgService.withdraw(golddbo);
						scoresMsgService.withdraw(scoredbo);
						orderService.updateDeliveTime(order.getOut_trade_no(), System.currentTimeMillis());
						orderService.updateOrderStatus(order.getOut_trade_no(), 1);
						// kafka发消息
						order = orderService.findOrderByOut_trade_no(order.getOut_trade_no());
						ordersMsgService.updateOrder(order);
					} catch (MemberNotFoundException e) {
						e.printStackTrace();
					}
				} else {
					orderService.updateOrderStatus(order.getOut_trade_no(), -1);
				}
			}
			vo.setSuccess(true);
			vo.setMsg("success");
			vo.setData(responseMap);
		} catch (Exception e) {
			e.printStackTrace();
			vo.setSuccess(false);
			vo.setMsg("fail");
		}
		return vo;
	}

	@RequestMapping("/closewxorder")
	public CommonVO closeWXOrder(String out_trade_no) {
		CommonVO vo = new CommonVO();
		Order order = orderService.findOrderByOut_trade_no(out_trade_no);
		if ((System.currentTimeMillis() - order.getCreateTime()) < 300000) {
			try {
				SortedMap<String, String> responseMap = wxpayService.closeOrder(out_trade_no);
				if (responseMap != null && "SUCCESS".equals(responseMap.get("return_code"))
						&& "SUCCESS".equals(responseMap.get("result_code"))) {
					if (!orderService.updateOrderStatus(out_trade_no, -1)) {
						vo.setSuccess(false);
						vo.setMsg("update order status fail");
						vo.setData(responseMap);
						return vo;
					}
				}
				vo.setSuccess(true);
				vo.setMsg("success");
				vo.setData(responseMap);
			} catch (Exception e) {
				e.printStackTrace();
				vo.setSuccess(false);
				vo.setMsg("fail");
			}
		} else {
			vo.setSuccess(false);
			vo.setMsg("try frequently");
		}
		return vo;
	}

	@RequestMapping("/wxrefund")
	public CommonVO refundWX(String out_trade_no, String refund_fee, String refund_desc) {
		CommonVO vo = new CommonVO();
		RefundOrder refundOrder = refundOrderService.addRefundOrder(out_trade_no, refund_fee, refund_desc);
		try {
			SortedMap<String, String> responseMap = wxpayService.createRefund(refundOrder);
			if (responseMap != null && "SUCCESS".equals(responseMap.get("return_code"))
					&& "SUCCESS".equals(responseMap.get("result_code"))) {
				if (!refundOrderService.updateRefundOrderStatus(refundOrder.getOut_refund_no(),
						responseMap.get("refund_id"))) {
					vo.setSuccess(false);
					vo.setMsg("update refund_id fail");
					vo.setData(responseMap);
					return vo;
				}
			}
			vo.setSuccess(true);
			vo.setMsg("success");
			vo.setData(responseMap);
		} catch (Exception e) {
			e.printStackTrace();
			vo.setSuccess(false);
			vo.setMsg("fail");
		}
		return vo;
	}

	@RequestMapping("/querywxrefund")
	public CommonVO queryWXRefund(String refund_id) {
		CommonVO vo = new CommonVO();
		try {
			SortedMap<String, String> responseMap = wxpayService.queryRefundResult(refund_id);
			vo.setSuccess(true);
			vo.setMsg("success");
			vo.setData(responseMap);
		} catch (Exception e) {
			e.printStackTrace();
			vo.setSuccess(false);
			vo.setMsg("fail");
		}
		return vo;
	}
}
