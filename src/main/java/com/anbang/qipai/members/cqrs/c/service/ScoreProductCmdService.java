package com.anbang.qipai.members.cqrs.c.service;

public interface ScoreProductCmdService {

	Integer buyProduct(String id, Integer amount) throws Exception;

	Integer addProduct(String id, Integer amount) throws Exception;

	void clear() throws Exception;

	void fill(String id, Integer remain) throws Exception;
}
