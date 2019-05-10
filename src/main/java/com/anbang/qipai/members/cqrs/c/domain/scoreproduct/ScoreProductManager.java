package com.anbang.qipai.members.cqrs.c.domain.scoreproduct;

import java.util.HashMap;
import java.util.Map;

/**
 * 礼券商品管理
 * 
 * @author lsc
 *
 */
public class ScoreProductManager {

	private Map<String, ScoreProduct> idProductMap = new HashMap<>();

	/**
	 * 购买
	 */
	public int buyProduct(String id, int amount) throws ProductNotFoundException, ProductNotEnoughException {
		if (!idProductMap.containsKey(id)) {
			throw new ProductNotFoundException();
		}
		ScoreProduct product = idProductMap.get(id);
		product.buy(amount);
		return product.getRemain();
	}

	/**
	 * 添加
	 */
	public int addProduct(String id, int amount) throws ProductNotFoundException {
		if (!idProductMap.containsKey(id)) {
			throw new ProductNotFoundException();
		}
		ScoreProduct product = idProductMap.get(id);
		product.add(amount);
		return product.getRemain();
	}

	/**
	 * 清空
	 */
	public void clear() {
		idProductMap.clear();
	}

	/**
	 * 填充
	 */
	public void fill(String id, int remain) {
		ScoreProduct product = new ScoreProduct();
		product.setId(id);
		product.setRemain(remain);
		idProductMap.put(product.getId(), product);
	}

	public Map<String, ScoreProduct> getIdProductMap() {
		return idProductMap;
	}

	public void setIdProductMap(Map<String, ScoreProduct> idProductMap) {
		this.idProductMap = idProductMap;
	}
}
