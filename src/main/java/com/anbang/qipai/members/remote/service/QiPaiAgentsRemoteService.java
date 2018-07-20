package com.anbang.qipai.members.remote.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.anbang.qipai.members.remote.vo.CommonRemoteVO;

@FeignClient("qipai-agents")
public interface QiPaiAgentsRemoteService {
	@RequestMapping(value = "/invitation/invitemember")
	public CommonRemoteVO agent_invitemember(@RequestParam(value = "memberId") String memberId,
			@RequestParam(value = "nickname") String nickname,
			@RequestParam(value = "invitationCode") String invitationCode);
}
