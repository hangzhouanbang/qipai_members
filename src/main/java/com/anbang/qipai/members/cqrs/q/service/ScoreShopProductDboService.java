package com.anbang.qipai.members.cqrs.q.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.cqrs.q.dao.ScoreShopProductDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.ScoreShopProductDbo;
import com.anbang.qipai.members.plan.bean.ProductType;
import com.anbang.qipai.members.plan.dao.ProductTypeDao;
import com.highto.framework.web.page.ListPage;

@Service
public class ScoreShopProductDboService {

	@Autowired
	private ScoreShopProductDboDao scoreShopProductDboDao;

	@Autowired
	private ProductTypeDao productTypeDao;

	public void saveProductType(ProductType type) {
		productTypeDao.save(type);
	}

	public void removeProductTypeByIds(String[] ids) {
		productTypeDao.removeByIds(ids);
	}

	public void updateDescProductTypeById(String id, String desc) {
		productTypeDao.updateDescById(id, desc);
	}

	public List<ProductType> findAllProductType() {
		return productTypeDao.findAll();
	}

	public void saveScoreShopProductDbo(ScoreShopProductDbo dbo) {
		scoreShopProductDboDao.save(dbo);
	}

	public void updateScoreShopProductDbo(ScoreShopProductDbo dbo) {
		scoreShopProductDboDao.update(dbo);
	}

	public void removeScoreShopProductDboByIds(String[] ids) {
		scoreShopProductDboDao.removeByIds(ids);
	}

	public void saveAllScoreShopProductDbo(List<ScoreShopProductDbo> products) {
		scoreShopProductDboDao.removeAll();
		scoreShopProductDboDao.saveAll(products);
	}

	public ScoreShopProductDbo findScoreShopProductDboById(String id) {
		return scoreShopProductDboDao.findById(id);
	}

	public ListPage findScoreShopProductDboByType(int page, int size, String type) {
		int amount = (int) scoreShopProductDboDao.countByType(type);
		List<ScoreShopProductDbo> list = scoreShopProductDboDao.findByType(page, size, type);
		return new ListPage(list, page, size, amount);
	}

	public void incScoreShopProductDboRemainById(String id, int amount) {
		scoreShopProductDboDao.incRemainById(id, amount);
	}
}
