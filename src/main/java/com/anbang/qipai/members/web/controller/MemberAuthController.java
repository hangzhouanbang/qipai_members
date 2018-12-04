package com.anbang.qipai.members.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.msg.service.MemberLoginLimitRecordMsgService;
import com.anbang.qipai.members.plan.bean.MemberLoginLimitRecord;
import com.anbang.qipai.members.plan.service.MemberLoginLimitRecordService;
import com.anbang.qipai.members.web.vo.CommonVO;

@RestController
@RequestMapping("/auth")
public class MemberAuthController {

	@Autowired
	private MemberAuthService memberAuthService;

	@Autowired
	private MemberLoginLimitRecordService memberLoginLimitRecordService;

	@Autowired
	private MemberLoginLimitRecordMsgService memberLoginLimitRecordMsgService;

	@RequestMapping(value = "/trytoken")
	@ResponseBody
	public CommonVO trytoken(String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId != null) {
			MemberLoginLimitRecord record = memberLoginLimitRecordService.findByMemberId(memberId, true);
			if (record != null) {
				vo.setSuccess(false);
				vo.setMsg("login limited");
				return vo;
			}
			Map data = new HashMap();
			data.put("memberId", memberId);
			vo.setData(data);
		} else {
			vo.setSuccess(false);
		}

		return vo;
	}

	@RequestMapping(value = "/addlimit")
	@ResponseBody
	public CommonVO addLimit(@RequestBody MemberLoginLimitRecord record) {
		CommonVO vo = new CommonVO();
		memberLoginLimitRecordService.save(record);
		memberLoginLimitRecordMsgService.addrecord(record);
		vo.setSuccess(true);
		return vo;
	}

	@RequestMapping(value = "/deletelimits")
	@ResponseBody
	public CommonVO deleteLimits(@RequestBody String[] recordIds) {
		CommonVO vo = new CommonVO();
		memberLoginLimitRecordService.updateMemberLoginLimitRecordEfficientById(recordIds, false);
		memberLoginLimitRecordMsgService.deleterecords(recordIds);
		vo.setSuccess(true);
		return vo;
	}

}
