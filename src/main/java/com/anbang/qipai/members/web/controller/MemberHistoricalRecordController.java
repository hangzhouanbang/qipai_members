package com.anbang.qipai.members.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.service.MemberAuthService;
import com.anbang.qipai.members.plan.domain.historicalrecord.RuianHistoricalRecord;
import com.anbang.qipai.members.plan.domain.historicalrecord.WenZhouHistoricalRecord;
import com.anbang.qipai.members.plan.domain.historicalrecord.DianPaoHistoricalRecord;
import com.anbang.qipai.members.plan.domain.historicalrecord.Game;
import com.anbang.qipai.members.plan.domain.historicalrecord.MemberHistoricalRecord;
import com.anbang.qipai.members.plan.service.HistoricalRecordService;
import com.anbang.qipai.members.web.vo.CommonVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**历史战绩controller
 * @author 程佳 2018.6.19
 * **/
@RestController
@RequestMapping("/record")
public class MemberHistoricalRecordController {
	
	private static Logger logger = LoggerFactory.getLogger(MemberHistoricalRecordController.class);
	
	@Autowired
	private MemberAuthService memberAuthService;

	@Autowired
	private HistoricalRecordService historicalRecordService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/addrecord")
	@ResponseBody
	public CommonVO addRecord() throws MemberNotFoundException {
		String json = "{\"id\": \"101\",\"memberId\": \"362812\",\"game\": \"ruianMajiang\",\"endTime\": 1528539077777,\"ruian\": [{\"id\": \"5b1ba77a1fe520265c84b786\",\"memberid\": \"362812\",\"headimgurl\": \"http://p2.so.qhmsg.com/bdr/_240_/t01a4341599641be697.png\",\"roomid\": \"123456\",\"hucount\": 5,\"gamecount\": 8,\"reward\": 30,\"paocount\": 15,\"maxhucount\": 120,\"totalscore\": 85,\"endtime\": 1528698790919}, {\"id\": \"5b1ba77a1fe520265c84b786\",\"memberid\": \"599329\",\"roomid\": \"123456\",\"hucount\": 4,\"paocount\": 14,\"maxhucount\": 119,\"gamecount\": 8,\"reward\": 40,\"totalscore\": 95,\"endtime\": 1528698790919}, {\"id\": \"5b1ba77a1fe520265c84b786\",\"memberid\": \"172762\",\"roomid\": \"123456\",\"hucount\": 3,\"paocount\": 13,\"maxhucount\": 100,\"gamecount\": 8,\"reward\": 50,\"totalscore\": 125,\"endtime\": 1528698790919}, {\"id\": \"5b1ba77a1fe520265c84b786\",\"memberid\": \"zlspz\",\"roomid\": \"123456\",\"hucount\": 2,\"paocount\": 2,\"maxhucount\": 12,\"gamecount\": 8,\"reward\": 20,\"totalscore\": 8,\"endtime\": 1528698790919}],\"wenzhou\": [{}],\"dianpao\": [{}]}";
		JSONObject jsonobj = JSONObject.fromObject(json);
		JSONArray array = jsonobj.getJSONArray("ruian");
		JSONArray jsonarray = JSONArray.fromObject(array);
		JSONArray array1 = jsonobj.getJSONArray("wenzhou");
		JSONArray jsonarray1 = JSONArray.fromObject(array1);
		JSONArray array2 = jsonobj.getJSONArray("dianpao");
		JSONArray jsonarray2 = JSONArray.fromObject(array2);
		JSONObject jsongame = jsonobj.getJSONObject("game");
		Game game = (Game) JSONObject.toBean(jsongame,Game.class);
		List<RuianHistoricalRecord> lists = (List<RuianHistoricalRecord>) JSONArray.toCollection(jsonarray,RuianHistoricalRecord.class);
		List<WenZhouHistoricalRecord> lists1 = (List<WenZhouHistoricalRecord>) JSONArray.toCollection(jsonarray1,WenZhouHistoricalRecord.class);
		List<DianPaoHistoricalRecord> lists2 = (List<DianPaoHistoricalRecord>) JSONArray.toCollection(jsonarray2,DianPaoHistoricalRecord.class);
		MemberHistoricalRecord memberHistoricalRecord = (MemberHistoricalRecord) JSONObject.toBean(jsonobj,MemberHistoricalRecord.class);
		memberHistoricalRecord.setRuian(lists);
		memberHistoricalRecord.setWenzhou(lists1);
		memberHistoricalRecord.setDianpao(lists2);
		memberHistoricalRecord.setEndTime(Long.parseLong(jsonobj.getString("endTime")));
		memberHistoricalRecord.setId(jsonobj.getString("id"));
		memberHistoricalRecord.setGame(game);
		memberHistoricalRecord.setMemberId(jsonobj.getString("memberId"));
		historicalRecordService.addRecord(memberHistoricalRecord);
		return new CommonVO();
	}
	
	/**查询一个用户最近的20条记录
	 * **/
	@RequestMapping("/findrecord")
	@ResponseBody
	public CommonVO findRecord(String token) {
		CommonVO co = new CommonVO();
		if(token == null) {
			co.setSuccess(false);
			co.setMsg("invalid token");
			return co;
		}
		String memberId = memberAuthService.getMemberIdBySessionId(token);
		System.out.println("id"+memberId);
		List<MemberHistoricalRecord> lists = historicalRecordService.findAllRecord(memberId);
		co.setData(lists);
		return co;
	}
	
	/**用户点击查询单个历史战绩详情
	 ***/
	@RequestMapping("/findonerecord")
	@ResponseBody
	public CommonVO findOneRecord(String token,String id) {
		CommonVO co = new CommonVO();
		List<RuianHistoricalRecord> lists = new ArrayList<RuianHistoricalRecord>();
//		if(token == null) {
//			co.setSuccess(false);
//			co.setMsg("invalid token");
//			return co;
//		}
//		String memberId = memberAuthService.getMemberIdBySessionId(token);
		if(id != null && !id.equals("")) {
			MemberHistoricalRecord memberHistoricalRecord = historicalRecordService.findOneRecord(id);
			lists = memberHistoricalRecord.getRuian();
		}
		co.setData(lists);
		return co;
	}
	
}
