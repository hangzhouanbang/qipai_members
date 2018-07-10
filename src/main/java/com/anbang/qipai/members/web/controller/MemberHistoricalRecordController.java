package com.anbang.qipai.members.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.msg.service.HistoricaRecordMsgService;
import com.anbang.qipai.members.plan.domain.historicalrecord.DianPaoHistoricalRecord;
import com.anbang.qipai.members.plan.domain.historicalrecord.MemberHistoricalRecord;
import com.anbang.qipai.members.plan.domain.historicalrecord.RuianHistoricalRecord;
import com.anbang.qipai.members.plan.domain.historicalrecord.WenZhouHistoricalRecord;
import com.anbang.qipai.members.plan.service.HistoricalRecordService;
import com.anbang.qipai.members.web.vo.CommonVO;
import com.google.gson.Gson;

/**
 * 历史战绩controller
 * 
 * @author 程佳 2018.6.19
 **/
@RestController
@RequestMapping("/record")
public class MemberHistoricalRecordController {

	@Autowired
	private MemberAuthService memberAuthService;

	@Autowired
	private HistoricalRecordService historicalRecordService;

	@Autowired
	private HistoricaRecordMsgService historicaRecordMsgService;

	private Gson gson = new Gson();

	@RequestMapping("/addrecord")
	@ResponseBody
	public CommonVO addRecord() throws MemberNotFoundException {
		String json = "{\"id\": \"101\",\"memberId\": \"362812\",\"game\": \"ruianMajiang\",\"endTime\": 1528539077777,\"ruian\": [{\"id\": \"5b1ba77a1fe520265c84b786\",\"memberId\": \"362812\",\"headImgUrl\": \"http://p2.so.qhmsg.com/bdr/_240_/t01a4341599641be697.png\",\"roomId\": \"123456\",\"huCount\": 5,\"gameCount\": 8,\"reward\": 30,\"paoCount\": 15,\"maxHuCount\": 120,\"totalScore\": 85,\"endTime\": 1528698790919}, {\"id\": \"5b1ba77a1fe520265c84b786\",\"memberId\": \"599329\",\"roomId\": \"123456\",\"huCount\": 4,\"paoCount\": 14,\"maxHuCount\": 119,\"gameCount\": 8,\"reward\": 40,\"totalScore\": 95,\"endTime\": 1528698790919}, {\"id\": \"5b1ba77a1fe520265c84b786\",\"memberId\": \"172762\",\"roomId\": \"123456\",\"huCount\": 3,\"paoCount\": 13,\"maxhuCount\": 100,\"gameCount\": 8,\"reward\": 50,\"totalScore\": 125,\"endTime\": 1528698790919}, {\"id\": \"5b1ba77a1fe520265c84b786\",\"memberId\": \"zlspz\",\"roomId\": \"123456\",\"huCount\": 2,\"paoCount\": 2,\"maxHuCount\": 12,\"gameCount\": 8,\"reward\": 20,\"totalScore\": 8,\"endTime\": 1528698790919}],\"wenzhou\": [],\"dianpao\": []}";
		// String json = "{\"id\": \"101\",\"memberId\": \"362812\",\"game\":
		// \"wenzhouMajiang\",\"endTime\": 1528539077777,\"wenzhou\": [{\"id\":
		// \"5b1ba77a1fe520265c84b786\",\"memberId\": \"362812\",\"mammonCount\":
		// 3,\"sfCount\": 3,\"zimoCount\": 3,\"headImgUrl\":
		// \"http://p2.so.qhmsg.com/bdr/_240_/t01a4341599641be697.png\",\"roomId\":
		// \"123456\",\"huCount\": 5,\"gameCount\": 8,\"reward\": 30,\"totalScore\":
		// 85,\"endTime\": 1528698790919}, {\"id\":
		// \"5b1ba77a1fe520265c84b786\",\"memberId\": \"599329\",\"roomId\":
		// \"123456\",\"huCount\": 4,\"mammonCount\": 3,\"sfCount\": 3,\"zimoCount\":
		// 3,\"gameCount\": 8,\"reward\": 40,\"totalScore\": 95,\"endTime\":
		// 1528698790919}, {\"id\": \"5b1ba77a1fe520265c84b786\",\"memberId\":
		// \"172762\",\"roomId\": \"123456\",\"huCount\": 3,\"mammonCount\":
		// 3,\"sfCount\": 3,\"zimoCount\": 3,\"gameCount\": 8,\"reward\":
		// 50,\"totalScore\": 125,\"endTime\": 1528698790919}, {\"id\":
		// \"5b1ba77a1fe520265c84b786\",\"memberId\": \"zlspz\",\"roomId\":
		// \"123456\",\"huCount\": 2,\"mammonCount\": 3,\"sfCount\": 3,\"zimoCount\":
		// 3,\"gameCount\": 8,\"reward\": 20,\"totalScore\": 8,\"endTime\":
		// 1528698790919}],\"ruian\": [],\"dianpao\": []}";
		MemberHistoricalRecord memberHistoricalRecord = (MemberHistoricalRecord) gson.fromJson(json,
				MemberHistoricalRecord.class);
		historicalRecordService.addRecord(memberHistoricalRecord);
		historicaRecordMsgService.createHistoricaRecord(memberHistoricalRecord);
		return new CommonVO();
	}

	/**
	 * 查询一个用户最近的20条记录
	 **/
	@RequestMapping("/findrecord")
	@ResponseBody
	public CommonVO findRecord(String token) {
		CommonVO co = new CommonVO();
		if (token == null) {
			co.setSuccess(false);
			co.setMsg("invalid token");
			return co;
		}
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		List<MemberHistoricalRecord> lists = historicalRecordService.findAllRecord(memberId);
		co.setData(lists);
		return co;
	}

	/**
	 * 用户点击查询单个历史战绩详情
	 ***/
	@RequestMapping("/findonerecord")
	@ResponseBody
	public CommonVO findOneRecord(String token, String id) {
		CommonVO co = new CommonVO();
		// if(token == null) {
		// co.setSuccess(false);
		// co.setMsg("invalid token");
		// return co;
		// }
		// String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (id != null && !id.equals("")) {
			MemberHistoricalRecord memberHistoricalRecord = historicalRecordService.findOneRecord(id);
			if (memberHistoricalRecord.getRuian() != null) {
				List<RuianHistoricalRecord> lists = memberHistoricalRecord.getRuian();
				co.setData(lists);
				return co;
			}
			if (memberHistoricalRecord.getDianpao() != null) {
				List<DianPaoHistoricalRecord> lists1 = memberHistoricalRecord.getDianpao();
				co.setData(lists1);
				return co;
			}
			if (memberHistoricalRecord.getWenzhou() != null) {
				List<WenZhouHistoricalRecord> lists2 = memberHistoricalRecord.getWenzhou();
				co.setData(lists2);
				return co;
			}
		}
		co.setSuccess(false);
		co.setMsg("invalid id");
		return co;
	}

}
