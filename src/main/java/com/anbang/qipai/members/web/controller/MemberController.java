package com.anbang.qipai.members.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldAccountDbo;
import com.anbang.qipai.members.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberGoldQueryService;
import com.anbang.qipai.members.plan.domain.Member;
import com.anbang.qipai.members.plan.service.MemberService;
import com.anbang.qipai.members.web.vo.DetailsVo;
import com.anbang.qipai.members.web.vo.MemberVO;

@RestController
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private MemberAuthQueryService memberAuthQueryService;

	@Autowired
	private MemberGoldQueryService memberGoldQueryService;

	@Autowired
	private MemberAuthService memberAuthService;

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

//	@RequestMapping("/queryMember")
//	public DetailsVo queryMember(String token) {
//		String memberId = memberAuthService.getMemberIdBySessionId(token);
//		Member member = memberService.findMember(memberId);
//		DetailsVo dv = new DetailsVo();
//		dv.setVipLevel(member.getVipLevel());
//		dv.setPhone(member.getPhone());
//		long time = member.getVipEndTime();
//		long nowTime = System.currentTimeMillis();
//		long day = (time - nowTime) / (1000 * 60 * 60 * 24);
//		dv.setVipEndTime("剩余" + day + "天");
//		return dv;
//	}
//
//	@RequestMapping("/registerPhone")
//	public Map<String, Object> registerPhone(String phone, String token) {
//		String memberId = memberAuthService.getMemberIdBySessionId(token);
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("phone", phone);
//		if (memberId == null || phone == null) {
//			map.put("result", false);
//			return map;
//		}
//		memberService.registerPhone(memberId, phone);
//		map.put("result", true);
//		return map;
//	}

	@RequestMapping("/checkAccount")
	public Map<String, Object> checkAccount(@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "size", defaultValue = "10") Integer size, String token) {
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		MemberGoldAccountDbo accountId = memberGoldQueryService.findMemberGoldAccount(memberId);
		Map<String, Object> map = memberGoldQueryService.findMemberGoldRecords(page, size, accountId.getId());
		return map;
	}
}
