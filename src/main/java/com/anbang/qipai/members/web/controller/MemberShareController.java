package com.anbang.qipai.members.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.plan.service.ShareService;
import com.anbang.qipai.members.web.vo.CommonVO;

/**分享奖励controller
 * @author 程佳 2018.6.15
 * **/
@RestController
@RequestMapping("/share")
public class MemberShareController {
	
	@Autowired
	private MemberAuthService memberAuthService;
	
	@Autowired
	private ShareService shareService;

	/**分享成功，给用户增加奖励，每天3次
	 * @param token 获得会员id
	 * @throws MemberNotFoundException 
	 * **/
	@RequestMapping("sharetime")
	@ResponseBody
	public CommonVO sharetime(String token) throws MemberNotFoundException {
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		CommonVO co = new CommonVO();
		if(token == null) {
			co.setSuccess(false);
			co.setMsg("invalid token");
		}
		System.out.println(memberId);
		Integer integral = shareService.Shareupdatecount(memberId);
		co.setData(integral);
		return co;
	}
}
