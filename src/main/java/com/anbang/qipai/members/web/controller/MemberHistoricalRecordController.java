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
import com.anbang.qipai.members.plan.domain.historicalrecord.HistoricalRecord;
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
	public CommonVO addrecord() throws MemberNotFoundException {
		String json = "{\"id\": \"101\",\"memberid\": \"881071\",\"wanfa\": \"9\",\"endtime\": 1528539077777,\"records\": [{\"id\": \"5b1ba77a1fe520265c84b786\",\"memberid\": \"881071\",\"headimgurl\": \"https://timgsa.baidu.com/timg? image&quality=80&size=b9999_10000&sec=1530015085321&di=9dcc8aa1b90175100590fc49e7788fe3&imgtype=0&src=http%3A%2F %2Fi1.hdslb.com%2Fbfs%2Fface%2F786571f7cee65f75c12199713471b819d153e87b.jpg\",\"roomid\": \"123456\",\"hucount\": 5,\"gamecount\": 8,\"reward\": 30,\"paocount\": 15,\"maxhucount\": 120,\"totalscore\": 85,\"endtime\": 1528698790919}, {\"id\": \"5b1ba77a1fe520265c84b786\",\"memberid\": \"2\",\"roomid\": \"123456\",\"hucount\": 4,\"paocount\": 14,\"maxhucount\": 119,\"gamecount\": 8,\"reward\": 40,\"totalscore\": 95,\"endtime\": 1528698790919}, {\"id\": \"5b1ba77a1fe520265c84b786\",\"memberid\": \"3\",\"roomid\": \"123456\",\"hucount\": 3,\"paocount\": 13,\"maxhucount\": 100,\"gamecount\": 8,\"reward\": 50,\"totalscore\": 125,\"endtime\": 1528698790919}, {\"id\": \"5b1ba77a1fe520265c84b786\",\"memberid\": \"4\",\"roomid\": \"123456\",\"hucount\": 2,\"paocount\": 2,\"maxhucount\": 12,\"gamecount\": 8,\"reward\": 20,\"totalscore\": 8,\"endtime\": 1528698790919}]}";
		JSONObject jsonobj = JSONObject.fromObject(json);
		JSONArray array = jsonobj.getJSONArray("records");
		JSONArray jsonarray = JSONArray.fromObject(array);
		String endtime = jsonobj.getString("endtime");
		
		List<HistoricalRecord> lists = (List<HistoricalRecord>) JSONArray.toCollection(jsonarray,HistoricalRecord.class);
		MemberHistoricalRecord memberHistoricalRecord = (MemberHistoricalRecord) JSONObject.toBean(jsonobj,MemberHistoricalRecord.class);
		memberHistoricalRecord.setRecords(lists);
		memberHistoricalRecord.setEndtime(Long.parseLong(jsonobj.getString("endtime")));
		memberHistoricalRecord.setId(jsonobj.getString("id"));
		memberHistoricalRecord.setWanfa(jsonobj.getString("wanfa"));
		memberHistoricalRecord.setMemberid(jsonobj.getString("memberid"));
		historicalRecordService.addrecord(memberHistoricalRecord);
		return new CommonVO();
	}
	
	/**查询一个用户最近的20条记录
	 * **/
	@RequestMapping("/findrecord")
	@ResponseBody
	public CommonVO findrecord(String token) {
		CommonVO co = new CommonVO();
//		String memberId = memberAuthService.getMemberIdBySessionId(token);
//		if(token == null) {
//			co.setSuccess(false);
//			co.setMsg("invalid token");
//		}
		String memberId = "881071";
		List<MemberHistoricalRecord> lists = historicalRecordService.findallrecord(memberId);
		co.setData(lists);
		return co;
	}
	
	/**用户点击查询单个历史战绩详情
	 ***/
	@RequestMapping("/findonerecord")
	@ResponseBody
	public CommonVO findonerecord(String token,String id) {
		CommonVO co = new CommonVO();
		List<HistoricalRecord> lists = new ArrayList<HistoricalRecord>();
//		String memberId = memberAuthService.getMemberIdBySessionId(token);
//		if(token == null) {
//			co.setSuccess(false);
//			co.setMsg("invalid token");
//		}
		if(id != null && !id.equals("")) {
			MemberHistoricalRecord memberHistoricalRecord = historicalRecordService.findonerecord(id);
			lists = memberHistoricalRecord.getRecords();
		}
		co.setData(lists);
		return co;
	}
	
}
