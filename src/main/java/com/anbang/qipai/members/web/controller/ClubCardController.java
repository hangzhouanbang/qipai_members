package com.anbang.qipai.members.web.controller;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	private RefundOrderService refundOrderService;

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

	@RequestMapping("/createalipayorder")
	public CommonVO createAliPayOrder(String memberId, String clubCardId, Integer number) {
		CommonVO vo = new CommonVO();
		Order order = orderService.addOrder(memberId, clubCardId, number, "alipay");
		try {
			String orderString = alipayService.order(order);
			vo.setSuccess(true);
			vo.setMsg("success");
			vo.setData(orderString);
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			vo.setSuccess(false);
			vo.setMsg("fail");
		}
		return vo;
	}

	@RequestMapping("/alipaynotify")
	public String alipayNotify(HttpServletRequest request) {
		if (alipayService.alipayNotify(request)) {
			return "success";
		}
		return "fail";
	}

	@RequestMapping("/checkalipay")
	public CommonVO checkAlipay(Long out_trade_no) {
		CommonVO vo = new CommonVO();
		if (alipayService.checkAlipay(String.valueOf(out_trade_no)) == 1) {
//			memberService.deliver(String.valueOf(out_trade_no), System.currentTimeMillis());
			vo.setSuccess(true);
			vo.setMsg("success");
			return vo;
		}
		vo.setSuccess(false);
		vo.setMsg("fail");
		return vo;
	}

	@RequestMapping("/createwxorder")
	public CommonVO createWXOrder(String memberId, String clubCardId, Integer number, HttpServletRequest request) {
		CommonVO vo = new CommonVO();
		Order order = orderService.addOrder(memberId, clubCardId, number, "WXpay");
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
	public CommonVO queryWXOrderResult(String transaction_id, String out_trade_no) {
		CommonVO vo = new CommonVO();
		try {
			SortedMap<String, String> responseMap = wxpayService.queryOrderResult(transaction_id);
			if (responseMap != null && "SUCCESS".equals(responseMap.get("return_code"))
					&& "SUCCESS".equals(responseMap.get("result_code"))) {
				memberService.deliver(out_trade_no, System.currentTimeMillis());
				if (!orderService.updateTransaction_id(out_trade_no, transaction_id)) {
					vo.setSuccess(false);
					vo.setMsg("update transaction_id fail");
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
