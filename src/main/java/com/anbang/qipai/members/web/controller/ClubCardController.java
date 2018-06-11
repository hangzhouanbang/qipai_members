package com.anbang.qipai.members.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.plan.domain.ClubCard;
import com.anbang.qipai.members.plan.service.ClubCardService;
import com.anbang.qipai.members.web.vo.CommonVO;

@RestController
@RequestMapping("/clubcard")
public class ClubCardController {

	@Autowired
	private ClubCardService clubCardService;
	@Autowired
	private MemberAuthService memberAuthService;

	@RequestMapping("/showclubcard")
	public CommonVO showClubCards() {
		CommonVO vo = new CommonVO();
		List<ClubCard> cardList = clubCardService.showClubCard();
		vo.setSuccess(true);
		vo.setMsg("会员卡列表");
		vo.setData(cardList);
		return vo;
	}
}
