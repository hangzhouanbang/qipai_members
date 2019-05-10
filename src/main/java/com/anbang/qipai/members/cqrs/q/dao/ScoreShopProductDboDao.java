package com.anbang.qipai.members.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.members.cqrs.q.dbo.ScoreShopProductDbo;

public interface ScoreShopProductDboDao {

	void save(ScoreShopProductDbo dbo);

	void update(ScoreShopProductDbo dbo);

	void removeByIds(String[] ids);

	void removeAll();

	void saveAll(List<ScoreShopProductDbo> products);

	ScoreShopProductDbo findById(String id);

	long countByType(String type);

	List<ScoreShopProductDbo> findByType(int page, int size, String type);

	void incRemainById(String id, int amount);
}
