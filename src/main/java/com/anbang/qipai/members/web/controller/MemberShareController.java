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

	/**微信好友分享成功，给用户增加奖励，每天3次
	 * @param token 获得会员id
	 * @throws MemberNotFoundException 
	 * **/
	@RequestMapping("/wxfirends_sharetime")
	@ResponseBody
	public CommonVO wxfirends_sharetime(String token) throws MemberNotFoundException {
//		String memberId = memberAuthService.getMemberIdBySessionId(token);
//		if(token == null) {
//			co.setSuccess(false);
//			co.setMsg("invalid token");
//		}
		String memberId = "881071";//测试id
		CommonVO co = shareService.wxfirends_sharetime(memberId);
		return co;
	}
	
	/**微信朋友圈分享成功，给用户增加奖励，每天1次
	 * @param token 获得会员id
	 * @throws MemberNotFoundException 
	 * **/
	@RequestMapping("/wxfirendscircle_sharetime")
	@ResponseBody
	public CommonVO wxfirendscircle_sharetime(String token) throws MemberNotFoundException {
	
		String memberId = "881071";//测试id
//		String memberId = memberAuthService.getMemberIdBySessionId(token);
//		if(token == null) {
//			co.setSuccess(false);
//			co.setMsg("invalid token");
//		}
		CommonVO co = shareService.wxfirendscircle_sharetime(memberId);
		return co;
	}
}
