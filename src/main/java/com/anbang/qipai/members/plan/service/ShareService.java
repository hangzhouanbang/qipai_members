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
import com.anbang.qipai.members.web.vo.CommonVO;

@Service
public class ShareService {
	
	@Autowired
	private ShareDao shareDao;
	
	@Autowired
	private MemberScoreCmdService memberScoreCmdService;

	public CommonVO wxfirends_sharetime(String memberid) throws MemberNotFoundException {
		CommonVO co = new CommonVO();
		Integer integral = 100;
		Share share = new Share();
		Share shares = shareDao.findShare(memberid);
		if(shares == null) {//新注册的用户第一次分享
			share.setId(memberid);
			share.setWxFriendsFrequency(1);
			share.setWxFirendsCircleFrequency(0);
			shareDao.Shareupdatecount(share);
			memberScoreCmdService.giveScoreToMember(memberid, integral, "share_reward", System.currentTimeMillis());
			co.setData(integral);
			return co;
		}else {
			if(shares.getWxFriendsFrequency() == 3) {
				co.setSuccess(false);
				return co;
			}else {
				share.setId(shares.getId());
				share.setWxFriendsFrequency(shares.getWxFriendsFrequency()+1);
				share.setWxFirendsCircleFrequency(shares.getWxFirendsCircleFrequency());
				shareDao.Shareupdatecount(share);
				memberScoreCmdService.giveScoreToMember(memberid, integral, "share_reward", System.currentTimeMillis());
				co.setData(integral);
				return co;
			}
		}
		
	}
	
	public CommonVO wxfirendscircle_sharetime(String memberid) throws MemberNotFoundException {
		CommonVO co = new CommonVO();
		Integer integral = 100;
		Share share = new Share();
		Share shares = shareDao.findShare(memberid);
		if(shares == null) {
			share.setId(memberid);
			share.setWxFirendsCircleFrequency(1);
			share.setWxFriendsFrequency(0);
			shareDao.Shareupdatecount(share);
			memberScoreCmdService.giveScoreToMember(memberid, integral, "share_reward", System.currentTimeMillis());
			co.setData(integral);
			return co;
		}else {
			if(shares.getWxFirendsCircleFrequency() == null) {
				share.setId(shares.getId());
				share.setWxFirendsCircleFrequency(1);
				share.setWxFriendsFrequency(shares.getWxFriendsFrequency());
				shareDao.Shareupdatecount(share);
				memberScoreCmdService.giveScoreToMember(memberid, integral, "share_reward", System.currentTimeMillis());
				co.setData(integral);
				return co;
			}
			co.setSuccess(false);
			return co;
		}
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
				share.setWxFriendsFrequency(0);
				share.setWxFirendsCircleFrequency(0);
				shareDao.Shareupdatecount(share);
			}
			page++;
		}
	}
	
	
}
