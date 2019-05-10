package com.anbang.qipai.members.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.c.domain.scoreproduct.ScoreProductManager;
import com.anbang.qipai.members.cqrs.c.service.ScoreProductCmdService;

@Component
public class ScoreProductCmdServiceImpl extends CmdServiceBase implements ScoreProductCmdService {

	@Override
	public Integer buyProduct(String id, Integer amount) throws Exception {
		ScoreProductManager scoreProductManager = this.singletonEntityRepository.getEntity(ScoreProductManager.class);
		return scoreProductManager.buyProduct(id, amount);
	}

	@Override
	public Integer addProduct(String id, Integer amount) throws Exception {
		ScoreProductManager scoreProductManager = this.singletonEntityRepository.getEntity(ScoreProductManager.class);
		return scoreProductManager.addProduct(id, amount);
	}

	@Override
	public void clear() throws Exception {
		ScoreProductManager scoreProductManager = this.singletonEntityRepository.getEntity(ScoreProductManager.class);
		scoreProductManager.clear();
	}

	@Override
	public void fill(String id, Integer remain) throws Exception {
		ScoreProductManager scoreProductManager = this.singletonEntityRepository.getEntity(ScoreProductManager.class);
		scoreProductManager.fill(id, remain);
	}

}
