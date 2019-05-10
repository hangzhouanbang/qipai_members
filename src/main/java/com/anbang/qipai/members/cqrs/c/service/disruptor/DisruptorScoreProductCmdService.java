package com.anbang.qipai.members.cqrs.c.service.disruptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.c.service.ScoreProductCmdService;
import com.anbang.qipai.members.cqrs.c.service.impl.ScoreProductCmdServiceImpl;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;
import com.highto.framework.ddd.NullObj;

@Component(value = "scoreProductCmdService")
public class DisruptorScoreProductCmdService extends DisruptorCmdServiceBase implements ScoreProductCmdService {

	@Autowired
	private ScoreProductCmdServiceImpl scoreProductCmdServiceImpl;

	@Override
	public Integer buyProduct(String id, Integer amount) throws Exception {
		CommonCommand cmd = new CommonCommand(ScoreProductCmdServiceImpl.class.getName(), "buyProduct", id, amount);
		DeferredResult<Integer> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			Integer remain = scoreProductCmdServiceImpl.buyProduct(cmd.getParameter(), cmd.getParameter());
			return remain;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Integer addProduct(String id, Integer amount) throws Exception {
		CommonCommand cmd = new CommonCommand(ScoreProductCmdServiceImpl.class.getName(), "addProduct", id, amount);
		DeferredResult<Integer> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			Integer remain = scoreProductCmdServiceImpl.addProduct(cmd.getParameter(), cmd.getParameter());
			return remain;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void clear() throws Exception {
		CommonCommand cmd = new CommonCommand(ScoreProductCmdServiceImpl.class.getName(), "clear");
		DeferredResult<NullObj> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			scoreProductCmdServiceImpl.clear();
			return new NullObj();
		});
		try {
			result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void fill(String id, Integer remain) throws Exception {
		CommonCommand cmd = new CommonCommand(ScoreProductCmdServiceImpl.class.getName(), "fill", id, remain);
		DeferredResult<NullObj> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			scoreProductCmdServiceImpl.fill(cmd.getParameter(), cmd.getParameter());
			return new NullObj();
		});
		try {
			result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

}
