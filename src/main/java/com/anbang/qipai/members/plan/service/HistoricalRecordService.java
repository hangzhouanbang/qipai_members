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
import com.anbang.qipai.members.plan.domain.historicalrecord.RuianHistoricalRecord;
import com.anbang.qipai.members.plan.domain.historicalrecord.MemberHistoricalRecord;

@Service
public class HistoricalRecordService {
	
	@Autowired
	private MemberAuthQueryService memberAuthQueryService;
	
	@Autowired
	private HistoricalRecordDao historicalRecordDao;
	
	@Autowired
	private MemberScoreCmdService memberScoreCmdService;

	public void addRecord(MemberHistoricalRecord memberHistoricalRecord) throws MemberNotFoundException {
		List<RuianHistoricalRecord> lists = memberHistoricalRecord.getRuian();
		Collections.sort(lists,lists.get(0));
		Collections.reverse(lists);
		//根据分数排名添加积分奖励
		for(int i = 0;i < lists.size();i++) {
			memberScoreCmdService.giveScoreToMember(lists.get(i).getMemberId(),lists.get(i).getReward(), "record_reward", System.currentTimeMillis());
				MemberDbo memberdao = memberAuthQueryService.findMember(lists.get(i).getMemberId());
				if(memberdao != null) {
					lists.get(i).setNickName(memberdao.getNickname());
					lists.get(i).setVipLevel(memberdao.getVipLevel());
					lists.get(i).setHeadImgUrl(memberdao.getHeadimgurl());
				}
		}
	
		for (RuianHistoricalRecord historicalRecord : lists) {
			MemberHistoricalRecord memberHistoricalRecords = new MemberHistoricalRecord();
			memberHistoricalRecords.setMemberId(historicalRecord.getMemberId());
			memberHistoricalRecords.setGame(memberHistoricalRecord.getGame());
			memberHistoricalRecords.setRuian(lists);
			memberHistoricalRecords.setEndTime(memberHistoricalRecord.getEndTime());
			historicalRecordDao.addRecord(memberHistoricalRecords);
		}
	}
	
	public List<MemberHistoricalRecord> findAllRecord(String memberid) {
		return historicalRecordDao.findAllRecord(memberid);
	}
	
	public MemberHistoricalRecord findOneRecord(String id){
		return historicalRecordDao.findOneRecord(id);
	}
	
}
