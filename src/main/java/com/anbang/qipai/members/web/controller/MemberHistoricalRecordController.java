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
	public CommonVO addRecord() {
		CommonVO vo = new CommonVO();
		vo.setSuccess(true);
		String json = "{\"id\": \"101\",\"memberId\": \"362812\",\"game\": \"ruianMajiang\",\"endTime\": 1528539077777,\"ruian\": ["
				+ "{\"id\": \"5b1ba77a1fe520265c84b786\",\"memberId\": \"362812\",\"headImgUrl\": \"http://p2.so.qhmsg.com/bdr/_240_/t01a4341599641be697.png\",\"nickName\": \"woyaowan\",\"vipLevel\": \"2\",\"roomId\": \"123456\",\"huCount\": 5,\"gameCount\": 4,\"totalCount\": 8,\"reward\": 30,\"paoCount\": 15,\"mammonCount\": 1,\"maxHuCount\": 120,\"totalScore\": 85,\"endTime\": 1528698790919},"
				+ "{\"id\": \"5b1ba77a1fe520265c84b786\",\"memberId\": \"599329\",\"headImgUrl\": \"http://p2.so.qhmsg.com/bdr/_240_/t01a4341599641be697.png\",\"nickName\": \"woyaowan2\",\"vipLevel\": \"1\",\"roomId\": \"123456\",\"huCount\": 4,\"gameCount\": 4,\"totalCount\": 8,\"reward\": 40,\"paoCount\": 14,\"mammonCount\": 2,\"maxHuCount\": 110,\"totalScore\": 95,\"endTime\": 1528698790919},"
				+ "{\"id\": \"5b1ba77a1fe520265c84b786\",\"memberId\": \"172762\",\"headImgUrl\": \"http://p2.so.qhmsg.com/bdr/_240_/t01a4341599641be697.png\",\"nickName\": \"woyaowan3\",\"vipLevel\": \"2\",\"roomId\": \"123456\",\"huCount\": 3,\"gameCount\": 4,\"totalCount\": 8,\"reward\": 50,\"paoCount\": 12,\"mammonCount\": 1,\"maxHuCount\": 130,\"totalScore\": 75,\"endTime\": 1528698790919},"
				+ "{\"id\": \"5b1ba77a1fe520265c84b786\",\"memberId\": \"zlspz\",\"headImgUrl\": \"http://p2.so.qhmsg.com/bdr/_240_/t01a4341599641be697.png\",\"nickName\": \"woyaowan4\",\"vipLevel\": \"3\",\"roomId\": \"123456\",\"huCount\": 2,\"gameCount\": 4,\"totalCount\": 8,\"reward\": 60,\"paoCount\": 13,\"mammonCount\": 0,\"maxHuCount\": 150,\"totalScore\": 65,\"endTime\": 1528698790919}],\"wenzhou\": [],\"dianpao\": []}";
		MemberHistoricalRecord memberHistoricalRecord = (MemberHistoricalRecord) gson.fromJson(json,
				MemberHistoricalRecord.class);
		try {
			historicalRecordService.addRecord(memberHistoricalRecord);
		} catch (MemberNotFoundException e) {
			vo.setSuccess(false);
			vo.setMsg("member not found");
			e.printStackTrace();
		}
		historicaRecordMsgService.createHistoricaRecord(memberHistoricalRecord);
		return vo;
	}

	/**
	 * 查询一个用户最近的20条记录
	 **/
	@RequestMapping("/findrecord")
	@ResponseBody
	public CommonVO findRecord(String token) {
		CommonVO vo = new CommonVO();
		if (token == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		// String memberId = memberAuthService.getMemberIdBySessionId(token);
		String memberId = "362812";
		List<MemberHistoricalRecord> lists = historicalRecordService.findAllRecord(memberId);
		vo.setData(lists);
		return vo;
	}

	/**
	 * 用户点击查询单个历史战绩详情
	 ***/
	@RequestMapping("/findonerecord")
	@ResponseBody
	public CommonVO findOneRecord(String token, String id) {
		CommonVO vo = new CommonVO();
		if (token == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if (id != null && !id.equals("")) {
			MemberHistoricalRecord memberHistoricalRecord = historicalRecordService.findOneRecord(id);
			if (memberHistoricalRecord.getRuian() != null) {
				List<RuianHistoricalRecord> lists = memberHistoricalRecord.getRuian();
				vo.setData(lists);
				return vo;
			}
			if (memberHistoricalRecord.getDianpao() != null) {
				List<DianPaoHistoricalRecord> lists1 = memberHistoricalRecord.getDianpao();
				vo.setData(lists1);
				return vo;
			}
			if (memberHistoricalRecord.getWenzhou() != null) {
				List<WenZhouHistoricalRecord> lists2 = memberHistoricalRecord.getWenzhou();
				vo.setData(lists2);
				return vo;
			}
		}
		vo.setSuccess(false);
		vo.setMsg("invalid id");
		return vo;
	}

}
