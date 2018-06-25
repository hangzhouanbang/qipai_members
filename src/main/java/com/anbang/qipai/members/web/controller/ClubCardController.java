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
import com.anbang.qipai.members.plan.service.AlipayService;
import com.anbang.qipai.members.plan.service.ClubCardService;
import com.anbang.qipai.members.plan.service.MemberService;
import com.anbang.qipai.members.plan.service.OrderService;
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
		// 根据发货时间判断是否重复发货
		if ("TRADE_SUCCESS".equals(order.getStatus()) && order.getDeliveTime() == null) {
			 memberService.updateVIP(order);
			 // 发送会员信息
			 membersMsgService.updateMember(memberService.findMemberById(order.getMemberId()));
			 try {
			 AccountingRecord goldrcd =
			 memberGoldCmdService.giveGoldToMember(order.getMemberId(),
			 order.getGold() * order.getNumber(), "give for buy clubcard",
			 System.currentTimeMillis());
			 AccountingRecord scorercd =
			 memberScoreCmdService.giveScoreToMember(order.getMemberId(),
			 order.getScore() * order.getNumber(), "give for buy clubcard",
			 System.currentTimeMillis());
			 MemberGoldRecordDbo golddbo =
			 memberGoldQueryService.withdraw(order.getMemberId(), goldrcd);
			 MemberScoreRecordDbo scoredbo =
			 memberScoreQueryService.withdraw(order.getMemberId(), scorercd);
			 // TODO: rcd发kafka
			 goldsMsgService.withdraw(golddbo);
			 scoresMsgService.withdraw(scoredbo);
			orderService.updateDeliveTime(order.getOut_trade_no(), System.currentTimeMillis());
			} catch (MemberNotFoundException e) {
				e.printStackTrace();
			}
		}
		// kafka发消息
		order = orderService.findOrderByOut_trade_no(order.getOut_trade_no());
		ordersMsgService.updateOrder(order);
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

	@RequestMapping("/wxnotify")
	public String wxNotify(HttpServletRequest request) {
		SortedMap<String, String> resultMap = wxpayService.receiveNotify(request);
		if (resultMap != null && "SUCCESS".equals(resultMap.get("return_code"))) {
			String newSign = wxpayService.createSign(resultMap);
			if (newSign.equals(resultMap.get("sign"))) {
				orderService.updateTransaction_id(resultMap.get("out_trade_no"), resultMap.get("transaction_id"));
				SortedMap<String, String> responseMap = null;
				try {
					responseMap = wxpayService.queryOrderResult(resultMap.get("transaction_id"));
				} catch (Exception e) {
					e.printStackTrace();
					return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[参数格式校验错误]]></return_msg></xml>";
				}
				if (responseMap != null && "SUCCESS".equals(responseMap.get("result_code"))) {
					String trade_state = responseMap.get("trade_state");
					orderService.updateOrderStatus(responseMap.get("out_trade_no"), trade_state);
					Order order = orderService.findOrderByOut_trade_no(responseMap.get("out_trade_no"));
					if ("SUCCESS".equals(order.getStatus()) && order.getDeliveTime() == null) {
						 memberService.updateVIP(order);
						 // 发送会员信息
						 membersMsgService.updateMember(memberService.findMemberById(order.getMemberId()));
						 try {
						 AccountingRecord goldrcd =
						 memberGoldCmdService.giveGoldToMember(order.getMemberId(),
						 order.getGold() * order.getNumber(), "give for buy clubcard",
						 System.currentTimeMillis());
						 AccountingRecord scorercd =
						 memberScoreCmdService.giveScoreToMember(order.getMemberId(),
						 order.getScore() * order.getNumber(), "give for buy clubcard",
						 System.currentTimeMillis());
						 MemberGoldRecordDbo golddbo =
						 memberGoldQueryService.withdraw(order.getMemberId(), goldrcd);
						 MemberScoreRecordDbo scoredbo =
						 memberScoreQueryService.withdraw(order.getMemberId(),
						 scorercd);
						 // TODO: rcd发kafka
						 goldsMsgService.withdraw(golddbo);
						 scoresMsgService.withdraw(scoredbo);
						orderService.updateDeliveTime(order.getOut_trade_no(), System.currentTimeMillis());
						} catch (MemberNotFoundException e) {
							e.printStackTrace();
						}
					}
					// kafka发消息
					order = orderService.findOrderByOut_trade_no(order.getOut_trade_no());
					ordersMsgService.updateOrder(order);
					return "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>";
				}
			}
		}
		return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[验签失败]]></return_msg></xml>";
	}

	@RequestMapping("/querywxorderresult")
	public CommonVO queryWXOrderResult(String transaction_id) {
		CommonVO vo = new CommonVO();
		vo.setSuccess(true);
		vo.setMsg("query result");
		try {
			SortedMap<String, String> responseMap = wxpayService.queryOrderResult(transaction_id);
			if ("SUCCESS".equals(responseMap.get("result_code"))) {
				String trade_state = responseMap.get("trade_state");
				orderService.updateOrderStatus(responseMap.get("out_trade_no"), trade_state);
				Order order = orderService.findOrderByOut_trade_no(responseMap.get("out_trade_no"));
				if ("SUCCESS".equals(order.getStatus()) && order.getDeliveTime() == null) {
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
					} catch (MemberNotFoundException e) {
						e.printStackTrace();
					}
				}
				// kafka发消息
				order = orderService.findOrderByOut_trade_no(responseMap.get("out_trade_no"));
				ordersMsgService.updateOrder(order);
				vo.setData(responseMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			vo.setSuccess(false);
			vo.setMsg("fail");
		}
		return vo;
	}

}
