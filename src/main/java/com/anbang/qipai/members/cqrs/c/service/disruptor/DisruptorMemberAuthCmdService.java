package com.anbang.qipai.members.cqrs.c.service.disruptor;

import org.springframework.beans.factory.annotation.Autowired;

import com.anbang.qipai.members.cqrs.c.service.MemberAuthCmdService;
import com.anbang.qipai.members.cqrs.c.service.impl.MemberAuthCmdServiceImpl;
import com.dml.users.AuthorizationAlreadyExistsException;
import com.dml.users.UserNotFoundException;
import com.highto.framework.ddd.CommonCommand;

public class DisruptorMemberAuthCmdService extends DisruptorCmdServiceBase implements MemberAuthCmdService {

	@Autowired
	private DisruptorFactory disruptorFactory;

	@Autowired
	private MemberAuthCmdServiceImpl memberAuthCmdServiceImpl;

	@Override
	public void addThirdAuth(String publisher, String uuid, String memberId)
			throws UserNotFoundException, AuthorizationAlreadyExistsException {
		CommonCommand cmd = new CommonCommand(MemberAuthCmdServiceImpl.class.getName(), "addThirdAuth", publisher, uuid,
				memberId);
		publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			memberAuthCmdServiceImpl.addThirdAuth(cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
			return null;
		});
	}

	@Override
	public String createMemberAndAddThirdAuth(String publisher, String uuid, long currentTime)
			throws AuthorizationAlreadyExistsException {
		CommonCommand cmd = new CommonCommand(MemberAuthCmdServiceImpl.class.getName(), "createMemberAndAddThirdAuth",
				publisher, uuid, currentTime);
		return publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			String memberId = memberAuthCmdServiceImpl.createMemberAndAddThirdAuth(cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter());
			return memberId;
		});
	}

}
