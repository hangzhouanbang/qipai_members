package com.anbang.qipai.members.cqrs.c.service.disruptor;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;

import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;
import com.highto.framework.disruptor.event.CommandEvent;
import com.lmax.disruptor.dsl.Disruptor;

public abstract class DisruptorCmdServiceBase {

	@Autowired
	protected DisruptorFactory disruptorFactory;

	protected <T> T publishEvent(Disruptor<CommandEvent> disruptor, CommonCommand cmd, Callable<T> callable) {
		DeferredResult<T> deferredResult = new DeferredResult<>();
		disruptor.publishEvent((event, sequence) -> {
			event.setCmd(cmd);
			event.setHandler(() -> {
				T returnObj = null;
				try {
					returnObj = callable.call();
				} catch (Exception e) {
					e.printStackTrace();
				}
				deferredResult.setResult(returnObj);
				return null;
			});

		});
		T result = getResult(deferredResult);
		return result;
	}

	private <T> T getResult(DeferredResult<T> deferredResult) {
		try {
			return deferredResult.getResult();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

}
