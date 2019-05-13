package com.anbang.qipai.members.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.config.ScoreProductRecordStatus;
import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.domain.scoreproduct.ProductNotEnoughException;
import com.anbang.qipai.members.cqrs.c.domain.scoreproduct.ProductNotFoundException;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.cqrs.c.service.MemberGoldCmdService;
import com.anbang.qipai.members.cqrs.c.service.MemberScoreCmdService;
import com.anbang.qipai.members.cqrs.c.service.ScoreProductCmdService;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldRecordDbo;
import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreRecordDbo;
import com.anbang.qipai.members.cqrs.q.dbo.ReceiverInfoDbo;
import com.anbang.qipai.members.cqrs.q.dbo.RewardType;
import com.anbang.qipai.members.cqrs.q.dbo.ScoreShopProductDbo;
import com.anbang.qipai.members.cqrs.q.service.MemberGoldQueryService;
import com.anbang.qipai.members.cqrs.q.service.MemberScoreQueryService;
import com.anbang.qipai.members.cqrs.q.service.ReceiverInfoQueryService;
import com.anbang.qipai.members.cqrs.q.service.ScoreShopProductDboService;
import com.anbang.qipai.members.msg.service.GoldsMsgService;
import com.anbang.qipai.members.msg.service.ProductTypeMsgService;
import com.anbang.qipai.members.msg.service.ScoreProductRecordMsgService;
import com.anbang.qipai.members.msg.service.ScoreShopProductMsgService;
import com.anbang.qipai.members.msg.service.ScoresMsgService;
import com.anbang.qipai.members.plan.bean.ProductType;
import com.anbang.qipai.members.plan.bean.ScoreProductRecord;
import com.anbang.qipai.members.util.IPUtil;
import com.anbang.qipai.members.web.vo.CommonVO;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.InsufficientBalanceException;
import com.highto.framework.web.page.ListPage;

@CrossOrigin
@RestController
@RequestMapping("/scoreshop")
public class ScoreShopController {

	@Autowired
	private MemberAuthService memberAuthService;

	@Autowired
	private ScoreShopProductDboService scoreShopProductDboService;

	@Autowired
	private ScoreProductCmdService scoreProductCmdService;

	@Autowired
	private MemberScoreCmdService memberScoreCmdService;

	@Autowired
	private MemberScoreQueryService memberScoreQueryService;

	@Autowired
	private ScoresMsgService scoresMsgService;

	@Autowired
	private MemberGoldCmdService memberGoldCmdService;

	@Autowired
	private MemberGoldQueryService memberGoldQueryService;

	@Autowired
	private GoldsMsgService goldsMsgService;

	@Autowired
	private ScoreProductRecordMsgService scoreProductRecordMsgService;

	@Autowired
	private ProductTypeMsgService productTypeMsgService;

	@Autowired
	private ScoreShopProductMsgService scoreShopProductMsgService;

	@Autowired
	private ReceiverInfoQueryService receiverInfoQueryService;

	/**
	 * 添加类目
	 */
	@RequestMapping("/addtype")
	public CommonVO addType(String desc) {
		CommonVO vo = new CommonVO();
		ProductType type = new ProductType();
		type.setDesc(desc);
		scoreShopProductDboService.saveProductType(type);
		productTypeMsgService.addType(type);
		vo.setSuccess(true);
		vo.setMsg("success");
		return vo;
	}

	/**
	 * 修改类目
	 */
	@RequestMapping("/updatetype")
	public CommonVO updateType(String id, String desc) {
		CommonVO vo = new CommonVO();
		ProductType type = scoreShopProductDboService.updateDescProductTypeById(id, desc);
		productTypeMsgService.updateType(type);
		vo.setSuccess(true);
		vo.setMsg("success");
		return vo;
	}

	/**
	 * 删除类目
	 */
	@RequestMapping("/removetype")
	public CommonVO removeType(@RequestBody String[] ids) {
		CommonVO vo = new CommonVO();
		scoreShopProductDboService.removeProductTypeByIds(ids);
		productTypeMsgService.removeType(ids);
		vo.setSuccess(true);
		vo.setMsg("success");
		return vo;
	}

	/**
	 * 所有类目
	 */
	@RequestMapping("/alltype")
	public CommonVO allType(String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		List<ProductType> list = scoreShopProductDboService.findAllProductType();
		vo.setSuccess(true);
		vo.setMsg("type list");
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("list", list);
		return vo;
	}

	/**
	 * 发布礼券商品
	 */
	@RequestMapping("/release")
	public CommonVO release(@RequestBody List<ScoreShopProductDbo> products) {
		CommonVO vo = new CommonVO();
		if (!products.isEmpty()) {
			scoreShopProductDboService.saveAllScoreShopProductDbo(products);
			try {
				scoreProductCmdService.clear();
				for (ScoreShopProductDbo dbo : products) {
					scoreProductCmdService.addProduct(dbo.getId(), dbo.getRemain());
				}
			} catch (Exception e) {
				scoreShopProductDboService.clearAllScoreShopProductDbo();
				e.printStackTrace();
			}
		}
		// scoreShopProductMsgService.release(products);
		return vo;
	}

	/**
	 * 根据类目查询商品
	 */
	@RequestMapping("/queryproduct")
	public CommonVO queryScoreProduct(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "20") int size, String type, String token) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		ListPage listPage = scoreShopProductDboService.findScoreShopProductDboByType(page, size, type);
		Map data = new HashMap<>();
		vo.setData(data);
		data.put("list", listPage);
		return vo;
	}

	/**
	 * 购买礼券商品
	 */
	@RequestMapping("/buyscoreproduct")
	public CommonVO buyScoreProduct(HttpServletRequest request, String token, String productId,
			@RequestParam(defaultValue = "1") int amount) {
		CommonVO vo = new CommonVO();
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (memberId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		ScoreShopProductDbo product = scoreShopProductDboService.findScoreShopProductDboById(productId);
		if (product == null) {
			vo.setSuccess(false);
			vo.setMsg("product not found");
			return vo;
		}
		ReceiverInfoDbo receiverInfo = receiverInfoQueryService.findReceiverByMemberId(memberId);
		if (receiverInfo == null) {
			vo.setSuccess(false);
			vo.setMsg("receiverInfo not found");
			return vo;
		}
		try {
			AccountingRecord rcd = memberScoreCmdService.withdraw(memberId, product.getPrice(),
					"buy " + product.getDesc(), System.currentTimeMillis());
			MemberScoreRecordDbo dbo = memberScoreQueryService.withdraw(memberId, rcd);
			scoresMsgService.withdraw(dbo);
			int remain = scoreProductCmdService.buyProduct(productId, amount);
			product = scoreShopProductDboService.upateScoreShopProductDboRemainById(productId, remain);
			scoreShopProductMsgService.update(product);
			String ip = IPUtil.getRealIp(request);
			reward(ip, product, memberId, amount);
		} catch (InsufficientBalanceException e) {
			vo.setSuccess(false);
			vo.setMsg("InsufficientBalanceException");
			return vo;
		} catch (MemberNotFoundException e) {
			vo.setSuccess(false);
			vo.setMsg("MemberNotFoundException");
			return vo;
		} catch (Exception e) {
			try {
				if (e instanceof ProductNotFoundException || e instanceof ProductNotEnoughException) {
					AccountingRecord rcd = memberScoreCmdService.giveScoreToMember(memberId, product.getPrice(),
							"refund " + product.getDesc(), System.currentTimeMillis());
					MemberScoreRecordDbo dbo = memberScoreQueryService.withdraw(memberId, rcd);
					scoresMsgService.withdraw(dbo);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		return vo;
	}

	private void reward(String requestIP, ScoreShopProductDbo product, String memberId, int amount) {
		ScoreProductRecord record = scoreShopProductDboService.saveScoreProductRecord(requestIP, product, memberId,
				amount);
		scoreProductRecordMsgService.addRecord(record);
		if (RewardType.YUSHI.equals(product.getRewardType())) {
			try {
				AccountingRecord rcd = memberGoldCmdService.giveGoldToMember(memberId, product.getRewardNum(),
						"score exchange", System.currentTimeMillis());
				MemberGoldRecordDbo dbo = memberGoldQueryService.withdraw(memberId, rcd);
				// rcd发kafka
				goldsMsgService.withdraw(dbo);
				record = scoreShopProductDboService.finishScoreProductRecord(record.getId(), System.currentTimeMillis(),
						ScoreProductRecordStatus.SUCCESS);
				scoreProductRecordMsgService.finishRecord(record);
			} catch (MemberNotFoundException e) {
				e.printStackTrace();
			}
		} else if (RewardType.ENTITY.equals(product.getRewardType())) {

		}
	}

	/**
	 * 通过商品发货审核
	 */
	@RequestMapping("/pass")
	public CommonVO passScoreProductRecord(String id) {
		CommonVO vo = new CommonVO();
		ScoreProductRecord record = scoreShopProductDboService.finishScoreProductRecord(id, System.currentTimeMillis(),
				ScoreProductRecordStatus.SUCCESS);
		scoreProductRecordMsgService.finishRecord(record);
		return vo;
	}

	/**
	 * 拒绝商品发货审核
	 */
	@RequestMapping("/refuse")
	public CommonVO refuseScoreProductRecord(String id) {
		CommonVO vo = new CommonVO();
		ScoreProductRecord record = scoreShopProductDboService.finishScoreProductRecord(id, System.currentTimeMillis(),
				ScoreProductRecordStatus.FAIL);
		scoreProductRecordMsgService.finishRecord(record);
		return vo;
	}
}
