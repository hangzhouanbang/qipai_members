package com.anbang.qipai.members.cqrs.c.service.disruptor;

import org.springframework.beans.factory.annotation.Autowired;

public class CoreSnapshotService {

	@Autowired
	private DisruptorFactory disruptorFactory;

	public void makeSnapshot() {
		disruptorFactory.getCoreCmdDisruptor().publishEvent((event, sequence) -> event.setSnapshot(true));
	}

}