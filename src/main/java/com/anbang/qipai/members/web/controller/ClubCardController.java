package com.anbang.qipai.members.web.controller;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.plan.domain.ClubCard;
import com.anbang.qipai.members.plan.domain.Order;
import com.anbang.qipai.members.plan.domain.RefundOrder;
import com.anbang.qipai.members.plan.service.ClubCardService;
import com.anbang.qipai.members.plan.service.MemberService;
import com.anbang.qipai.members.plan.service.OrderService;
import com.anbang.qipai.members.plan.service.RefundOrderService;
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

	@RequestMapping("/showclubcard")
	public CommonVO showClubCards() {
		CommonVO vo = new CommonVO();
		List<ClubCard> cardList = clubCardService.showClubCard();
		vo.setSuccess(true);
		vo.setMsg("会员卡列表");
		vo.setData(cardList);
		return vo;
	}

	@RequestMapping("/createorder")
	public CommonVO createOrder(String memberId, String clubCardId, Integer number, HttpServletRequest request) {
		CommonVO vo = new CommonVO();
		Order order = orderService.addOrder(memberId, clubCardId, number);
		try {
			Map<String, String> resultMap = clubCardService.createOrder(order, request.getRemoteAddr());
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

	@RequestMapping("/receivenotify")
	public String receiveNotify(HttpServletRequest request) {
		return clubCardService.receiveNotify(request);
	}

	@RequestMapping("/queryresult")
	public CommonVO queryOrderResult(String transaction_id, String out_trade_no, String memberId, String clubCardId) {
		CommonVO vo = new CommonVO();
		try {
			SortedMap<String, String> responseMap = clubCardService.queryOrderResult(transaction_id);
			if (responseMap != null && "SUCCESS".equals(responseMap.get("return_code"))
					&& "SUCCESS".equals(responseMap.get("result_code"))) {
				memberService.deliver(memberId, out_trade_no, clubCardId, System.currentTimeMillis());
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

	@RequestMapping("/closeorder")
	public CommonVO closeOrder(String out_trade_no) {
		CommonVO vo = new CommonVO();
		Order order = orderService.findOrderByOut_trade_no(out_trade_no);
		if ((System.currentTimeMillis() - order.getCreateTime()) < 300000) {
			try {
				SortedMap<String, String> responseMap = clubCardService.closeOrder(out_trade_no);
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

	@RequestMapping("/refund")
	public CommonVO refund(String out_trade_no, String refund_fee, String refund_desc) {
		CommonVO vo = new CommonVO();
		RefundOrder refundOrder = refundOrderService.addRefundOrder(out_trade_no, refund_fee, refund_desc);
		try {
			SortedMap<String, String> responseMap = clubCardService.createRefund(refundOrder);
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

	@RequestMapping("/queryrefund")
	public CommonVO queryRefund(String refund_id) {
		CommonVO vo = new CommonVO();
		try {
			SortedMap<String, String> responseMap = clubCardService.queryRefundResult(refund_id);
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
