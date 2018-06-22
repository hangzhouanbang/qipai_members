package com.anbang.qipai.members.plan.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.service.MemberScoreCmdService;
import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;
import com.anbang.qipai.members.cqrs.q.service.MemberAuthQueryService;
import com.anbang.qipai.members.plan.dao.HistoricalRecordDao;
import com.anbang.qipai.members.plan.domain.historicalrecord.HistoricalRecord;
import com.anbang.qipai.members.plan.domain.historicalrecord.MemberHistoricalRecord;

@Service
public class HistoricalRecordService {
	
	@Autowired
	private MemberAuthQueryService memberAuthQueryService;
	
	@Autowired
	private HistoricalRecordDao historicalRecordDao;
	
	@Autowired
	private MemberScoreCmdService memberScoreCmdService;

	public void addrecord(MemberHistoricalRecord memberHistoricalRecord) throws MemberNotFoundException {
		List<HistoricalRecord> lists = memberHistoricalRecord.getRecords();
		Collections.sort(lists,lists.get(0));
		Collections.reverse(lists);
		System.out.println("排序后：");
//		根据分数排名添加积分奖励
//		int amount = 50;
		for(int i = 0;i < lists.size();i++) {
//			memberScoreCmdService.giveScoreToMember(lists.get(i).getMemberid(), amount, "对战奖励", System.currentTimeMillis());
//			amount = amount - 10;
				MemberDbo memberdao = memberAuthQueryService.findMember(lists.get(i).getMemberid());
				if(memberdao != null) {
					lists.get(i).setNickname(memberdao.getNickname());
				}
		}
	
		for (HistoricalRecord historicalRecord : lists) {
			MemberHistoricalRecord memberHistoricalRecords = new MemberHistoricalRecord();
			memberHistoricalRecords.setMemberid(historicalRecord.getMemberid());
			memberHistoricalRecords.setWanfa(memberHistoricalRecord.getWanfa());
			memberHistoricalRecords.setRecords(lists);
			memberHistoricalRecords.setEndtime(historicalRecord.getEndtime());
			historicalRecordDao.addrecord(memberHistoricalRecords);
		}
	}
	
	public List<MemberHistoricalRecord> findallrecord(String memberid) {
		return historicalRecordDao.findallrecord(memberid);
	}
	
	public MemberHistoricalRecord findonerecord(String id){
		return historicalRecordDao.findonerecord(id);
	}
	
}