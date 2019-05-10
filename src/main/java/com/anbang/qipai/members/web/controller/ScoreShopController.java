package com.anbang.qipai.members.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.cqrs.c.service.ScoreProductCmdService;
import com.anbang.qipai.members.cqrs.q.service.ScoreShopProductDboService;
import com.anbang.qipai.members.plan.bean.ProductType;
import com.anbang.qipai.members.web.vo.CommonVO;

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

	@RequestMapping("/addtype")
	public CommonVO addType(String desc) {
		CommonVO vo = new CommonVO();
		ProductType type = new ProductType();
		type.setDesc(desc);
		scoreShopProductDboService.saveProductType(type);
		vo.setSuccess(true);
		vo.setMsg("success");
		return vo;
	}

	@RequestMapping("/updatetype")
	public CommonVO updateType(String id, String desc) {
		CommonVO vo = new CommonVO();
		scoreShopProductDboService.updateDescProductTypeById(id, desc);
		vo.setSuccess(true);
		vo.setMsg("success");
		return vo;
	}

	@RequestMapping("/removetype")
	public CommonVO removeType(@RequestBody String[] ids) {
		CommonVO vo = new CommonVO();
		scoreShopProductDboService.removeProductTypeByIds(ids);
		vo.setSuccess(true);
		vo.setMsg("success");
		return vo;
	}

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
}
