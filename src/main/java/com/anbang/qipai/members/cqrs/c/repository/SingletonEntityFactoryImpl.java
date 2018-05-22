package com.anbang.qipai.members.cqrs.c.repository;

import org.springframework.stereotype.Component;

import com.highto.framework.ddd.SingletonEntityFactory;

@Component(value = "entityFactory")
public class SingletonEntityFactoryImpl implements SingletonEntityFactory {

	@Override
	public <T> T createNew(Class<T> type) {
		try {
			return type.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

}
