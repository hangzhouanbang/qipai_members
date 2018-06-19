package com.anbang.qipai.members.plan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.anbang.qipai.members.cqrs.c.domain.MemberNotFoundException;
import com.anbang.qipai.members.cqrs.c.service.MemberScoreCmdService;
import com.anbang.qipai.members.plan.dao.ShareDao;
import com.anbang.qipai.members.plan.domain.Share;

@Service
public class ShareService {
	
	@Autowired
	private ShareDao shareDao;
	
	@Autowired
	private MemberScoreCmdService memberScoreCmdService;

	public Integer Shareupdatecount(String memberid) throws MemberNotFoundException {
		Integer integral = 100;
		String textSummary = "分享奖励";
		Share share = new Share();
		Share shares = shareDao.findShare(memberid);
		if(shares == null) {
			share.setId(memberid);
			share.setFrequency(1);
			memberScoreCmdService.giveScoreToMember(memberid, integral, textSummary, System.currentTimeMillis());
		}else {
			if(shares.getFrequency() == 3) {
				share.setId(shares.getId());
				share.setFrequency(shares.getFrequency());
			}else {
				share.setId(shares.getId());
				share.setFrequency(shares.getFrequency()+1);
				memberScoreCmdService.giveScoreToMember(memberid, integral, textSummary, System.currentTimeMillis());
			}
		}
		shareDao.Shareupdatecount(share);
		return integral;
	}
	
	/**分享奖励定时器
	 * **/
	@Scheduled(cron = "0 0 0 * * ?")//每天0点走这个方法
	public void sharetimer() {
		Integer page = 1;
		Integer size = 10000;
		Query query = new Query();
		Long count = shareDao.count(query, size);
		for(Integer i = 1;i<=count;i++) {//数据量太大，分页实现集合查询
			Pageable pageable= new PageRequest(page-1, size);
			List<Share> list = shareDao.pagingfind(query,pageable);
			for (Share share : list) {//分页查询后的会员更改分享次数
				share.setFrequency(0);
				shareDao.Shareupdatecount(share);
			}
			page++;
		}
	}
	
	
}
