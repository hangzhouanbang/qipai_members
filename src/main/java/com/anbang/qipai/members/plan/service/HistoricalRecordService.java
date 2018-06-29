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
import com.anbang.qipai.members.plan.domain.historicalrecord.WenZhouHistoricalRecord;
import com.anbang.qipai.members.plan.domain.historicalrecord.DianPaoHistoricalRecord;
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
		List<WenZhouHistoricalRecord> list1 = memberHistoricalRecord.getWenzhou();
		List<DianPaoHistoricalRecord> list2 = memberHistoricalRecord.getDianpao();
		if(lists.size() > 0) {
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
		if(list1.size() > 0) {
			Collections.sort(list1,list1.get(0));
			Collections.reverse(list1);
			//根据分数排名添加积分奖励
			for(int i = 0;i < list1.size();i++) {
				memberScoreCmdService.giveScoreToMember(list1.get(i).getMemberId(),list1.get(i).getReward(), "record_reward", System.currentTimeMillis());
					MemberDbo memberdao = memberAuthQueryService.findMember(list1.get(i).getMemberId());
					if(memberdao != null) {
						list1.get(i).setNickName(memberdao.getNickname());
						list1.get(i).setVipLevel(memberdao.getVipLevel());
						list1.get(i).setHeadImgUrl(memberdao.getHeadimgurl());
					}
			}
			for (WenZhouHistoricalRecord historicalRecord : list1) {
				MemberHistoricalRecord memberHistoricalRecords = new MemberHistoricalRecord();
				memberHistoricalRecords.setMemberId(historicalRecord.getMemberId());
				memberHistoricalRecords.setGame(memberHistoricalRecord.getGame());
				memberHistoricalRecords.setWenzhou(list1);
				memberHistoricalRecords.setEndTime(memberHistoricalRecord.getEndTime());
				historicalRecordDao.addRecord(memberHistoricalRecords);
			}
		}
		if(list2.size() > 0) {
			Collections.sort(list2,list2.get(0));
			Collections.reverse(list2);
			//根据分数排名添加积分奖励
			for(int i = 0;i < list2.size();i++) {
				memberScoreCmdService.giveScoreToMember(list2.get(i).getMemberId(),list2.get(i).getReward(), "record_reward", System.currentTimeMillis());
					MemberDbo memberdao = memberAuthQueryService.findMember(list2.get(i).getMemberId());
					if(memberdao != null) {
						list2.get(i).setNickName(memberdao.getNickname());
						list2.get(i).setVipLevel(memberdao.getVipLevel());
						list2.get(i).setHeadImgUrl(memberdao.getHeadimgurl());
					}
			}
			for (DianPaoHistoricalRecord historicalRecord : list2) {
				MemberHistoricalRecord memberHistoricalRecords = new MemberHistoricalRecord();
				memberHistoricalRecords.setMemberId(historicalRecord.getMemberId());
				memberHistoricalRecords.setGame(memberHistoricalRecord.getGame());
				memberHistoricalRecords.setDianpao(list2);
				memberHistoricalRecords.setEndTime(memberHistoricalRecord.getEndTime());
				historicalRecordDao.addRecord(memberHistoricalRecords);
			}
		}
	}
	
	public List<MemberHistoricalRecord> findAllRecord(String memberid) {
		return historicalRecordDao.findAllRecord(memberid);
	}
	
	public MemberHistoricalRecord findOneRecord(String id){
		return historicalRecordDao.findOneRecord(id);
	}
	
}
