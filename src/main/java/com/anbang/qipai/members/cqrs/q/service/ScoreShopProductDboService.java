package com.anbang.qipai.members.cqrs.q.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.config.ScoreProductRecordStatus;
import com.anbang.qipai.members.cqrs.q.dao.MemberDboDao;
import com.anbang.qipai.members.cqrs.q.dao.ReceiverInfoDboDao;
import com.anbang.qipai.members.cqrs.q.dao.ScoreShopProductDboDao;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.dbo.ReceiverInfoDbo;
import com.anbang.qipai.members.cqrs.q.dbo.ScoreShopProductDbo;
import com.anbang.qipai.members.plan.bean.ProductType;
import com.anbang.qipai.members.plan.bean.ScoreProductRecord;
import com.anbang.qipai.members.plan.dao.ProductTypeDao;
import com.anbang.qipai.members.plan.dao.ScoreProductRecordDao;
import com.anbang.qipai.members.util.IPAddressUtil;
import com.highto.framework.web.page.ListPage;

@Service
public class ScoreShopProductDboService {

	@Autowired
	private ReceiverInfoDboDao receiverInfoDboDao;

	@Autowired
	private MemberDboDao memberDboDao;

	@Autowired
	private ScoreShopProductDboDao scoreShopProductDboDao;

	@Autowired
	private ProductTypeDao productTypeDao;

	@Autowired
	private ScoreProductRecordDao scoreProductRecordDao;

	public ProductType findProductTypeById(String id) {
		return productTypeDao.findById(id);
	}

	public void saveProductType(ProductType type) {
		productTypeDao.save(type);
	}

	public void removeProductTypeByIds(String[] ids) {
		productTypeDao.removeByIds(ids);
	}

	public ProductType updateDescProductTypeById(String id, String desc) {
		productTypeDao.updateDescById(id, desc);
		return productTypeDao.findById(id);
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

	public void clearAllScoreShopProductDbo() {
		scoreShopProductDboDao.removeAll();
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

	public ScoreShopProductDbo upateScoreShopProductDboRemainById(String id, int remain) {
		scoreShopProductDboDao.updateRemainById(id, remain);
		return scoreShopProductDboDao.findById(id);
	}

	public ScoreProductRecord saveScoreProductRecord(String requestIP, ScoreShopProductDbo product, String memberId,
			int amount) {
		MemberDbo member = memberDboDao.findMemberById(memberId);
		ReceiverInfoDbo info = receiverInfoDboDao.findByMemberId(memberId);
		ScoreProductRecord record = new ScoreProductRecord();
		record.setAdress(info.getAddress());
		record.setAmount(amount);
		record.setCreateTime(System.currentTimeMillis());
		record.setHeadimgurl(member.getHeadimgurl());
		record.setIpLocation(IPAddressUtil.getIPAddress2(requestIP));
		record.setMemberId(memberId);
		record.setName(info.getName());
		record.setPhone(info.getTelephone());
		record.setNickname(member.getNickname());
		record.setProduct(product.getDesc());
		record.setReqIP(requestIP);
		record.setStatus(ScoreProductRecordStatus.PROCESS);
		scoreProductRecordDao.save(record);
		return record;
	}

	public ScoreProductRecord finishScoreProductRecord(String id, long deliverTime, String status) {
		scoreProductRecordDao.updateDeliverTimeById(id, deliverTime);
		scoreProductRecordDao.updateStatusById(id, status);
		return scoreProductRecordDao.findById(id);
	}

	public ListPage findScoreProductRecordByMemberId(int page, int size, String memberId) {
		int amount = (int) scoreProductRecordDao.countByMemberIdAndStatus(memberId, null);
		List<ScoreProductRecord> list = scoreProductRecordDao.findByMemberIdAndStatus(page, size, memberId, null);
		return new ListPage(list, page, size, amount);
	}
}
