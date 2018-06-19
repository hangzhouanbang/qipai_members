package com.anbang.qipai.members.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldAccountDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreAccountDbo;
import com.anbang.qipai.members.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberGoldQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberScoreQueryService;
import com.anbang.qipai.members.plan.service.MemberService;
import com.anbang.qipai.members.web.vo.CommonVO;
import com.anbang.qipai.members.web.vo.DetailsVo;
import com.anbang.qipai.members.web.vo.MemberVO;
import com.highto.framework.web.page.ListPage;

@CrossOrigin
@RestController
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MemberAuthService memberAuthService;
	@Autowired
	private MemberAuthQueryService memberAuthQueryService;

	@Autowired
	private MemberGoldQueryService memberGoldQueryService;

	@Autowired
	private MemberScoreQueryService memberScoreQueryService;

	@Autowired
	private MemberService memberService;

	@RequestMapping(value = "/info")
	@ResponseBody
	public MemberVO info(String memberId) {
		MemberVO vo = new MemberVO();
		MemberDbo memberDbo = memberAuthQueryService.findMember(memberId);
		if (memberDbo == null) {
			vo.setSuccess(false);
			return vo;
		}
		vo.setSuccess(true);
		vo.setHeadimgurl(memberDbo.getHeadimgurl());
		vo.setMemberId(memberId);
		vo.setNickname(memberDbo.getNickname());
		MemberGoldAccountDbo memberGoldAccountDbo = memberGoldQueryService.findMemberGoldAccount(memberId);
		if (memberGoldAccountDbo != null) {
			vo.setGold(memberGoldAccountDbo.getBalance());
		}
		return vo;
	}

	@RequestMapping("/querymember")
	public DetailsVo queryMember(String memberId) {
		DetailsVo vo = new DetailsVo();
		MemberDbo member = memberService.findMemberById(memberId);
		vo.setVipLevel(member.getVipLevel());
		vo.setPhone(member.getPhone());
		long time = member.getVipEndTime();
		long nowTime = System.currentTimeMillis();
		long day = (time - nowTime) / (1000 * 60 * 60 * 24);
		vo.setVipEndTime("剩余" + day + "天");
		vo.setSuccess(true);
		vo.setMsg("information");
		return vo;
	}

	@RequestMapping("/registerphone")
	public CommonVO registerPhone(String phone, String memberId) {
		CommonVO vo = new CommonVO();
		memberService.registerPhone(memberId, phone);
		vo.setSuccess(true);
		vo.setMsg("register success");
		vo.setData(phone);
		return vo;
	}

	@RequestMapping("/querygoldaccount")
	public CommonVO queryGoldAccount(@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "size", defaultValue = "10") Integer size, String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId != null) {
			MemberGoldAccountDbo accountId = memberGoldQueryService.findMemberGoldAccount(memberId);
			ListPage listPage = memberGoldQueryService.findMemberGoldRecords(page, size, accountId.getId());
			// ListPage listPage = memberGoldQueryService.findMemberGoldRecords(page, size,
			// "627532_gold_wallet");//测试用
			vo.setSuccess(true);
			vo.setMsg("goldaccout");
			vo.setData(listPage);
		} else {
			vo.setSuccess(false);
			vo.setMsg("No Such Member");
		}
		return vo;
	}

	@RequestMapping("/queryscoreaccount")
	public CommonVO queryScoreAccount(@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "size", defaultValue = "10") Integer size, String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId != null) {
			MemberScoreAccountDbo accountId = memberScoreQueryService.findMemberScoreAccount(memberId);
			ListPage listPage = memberScoreQueryService.findMemberScoreRecords(page, size, accountId.getId());
			// ListPage listPage = memberScoreQueryService.findMemberScoreRecords(page,
			// size, "627532_gold_wallet");// 测试用
			vo.setSuccess(true);
			vo.setMsg("scoreaccout");
			vo.setData(listPage);
		} else {
			vo.setSuccess(false);
			vo.setMsg("No Such Member");
		}
		return vo;
	}
}
