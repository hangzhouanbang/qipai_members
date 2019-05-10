package com.anbang.qipai.members.plan.dao;

import java.util.List;

import com.anbang.qipai.members.plan.bean.ProductType;

public interface ProductTypeDao {

	void save(ProductType type);

	void removeByIds(String[] ids);

	void updateDescById(String id, String desc);

	List<ProductType> findAll();
}
