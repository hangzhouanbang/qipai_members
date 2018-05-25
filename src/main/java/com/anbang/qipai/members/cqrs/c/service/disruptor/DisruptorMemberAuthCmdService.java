package com.anbang.qipai.members.cqrs.c.service.disruptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.c.service.MemberAuthCmdService;
import com.anbang.qipai.members.cqrs.c.service.impl.MemberAuthCmdServiceImpl;
import com.dml.users.AuthorizationAlreadyExistsException;
import com.dml.users.UserNotFoundException;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;

@Component(value = "memberAuthCmdService")
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
		DeferredResult<Object> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			memberAuthCmdServiceImpl.addThirdAuth(cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
			return null;
		});
		try {
			result.getResult();
		} catch (Exception e) {
			if (e instanceof UserNotFoundException) {
				throw (UserNotFoundException) e;
			} else if (e instanceof AuthorizationAlreadyExistsException) {
				throw (AuthorizationAlreadyExistsException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public String createMemberAndAddThirdAuth(String publisher, String uuid, Long currentTime)
			throws AuthorizationAlreadyExistsException {
		CommonCommand cmd = new CommonCommand(MemberAuthCmdServiceImpl.class.getName(), "createMemberAndAddThirdAuth",
				publisher, uuid, currentTime);
		DeferredResult<String> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			String memberId = memberAuthCmdServiceImpl.createMemberAndAddThirdAuth(cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter());
			return memberId;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			if (e instanceof AuthorizationAlreadyExistsException) {
				throw (AuthorizationAlreadyExistsException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

}
