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
import com.anbang.qipai.members.plan.service.ClubCardService;
import com.anbang.qipai.members.plan.service.MemberService;
import com.anbang.qipai.members.plan.service.OrderService;
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
	public CommonVO createOrder(String memberId, String clubCardId, Integer number) {
		CommonVO vo = new CommonVO();
		Order order = orderService.addOrder(memberId, clubCardId, number);
		try {
			Map<String, String> resultMap = clubCardService.createOrder(order);
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
	public CommonVO queryResult(String transaction_id, String out_trade_no, String memberId, String clubCardId) {
		CommonVO vo = new CommonVO();
		try {
			SortedMap<String, String> responseMap = clubCardService.queryResult(transaction_id);
			if (responseMap != null && "SUCCESS".equals(responseMap.get("return_code"))
					&& "SUCCESS".equals(responseMap.get("result_code"))) {
				memberService.deliver(memberId, out_trade_no, clubCardId, System.currentTimeMillis());
			}
			vo.setSuccess(true);
			vo.setMsg("success");
			vo.setData(responseMap);
		} catch (Exception e) {
			e.printStackTrace();
			vo.setSuccess(true);
			vo.setMsg("fail");
		}
		return vo;
	}
}
