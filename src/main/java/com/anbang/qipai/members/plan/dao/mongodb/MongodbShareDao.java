package com.anbang.qipai.members.plan.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.dao.ShareDao;
import com.anbang.qipai.members.plan.domain.Share;

@Component
public class MongodbShareDao implements ShareDao{

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public void Shareupdatecount(Share share) {
		mongoTemplate.save(share);
	}

	@Override
	public Share findShare(String memberid) {
		return mongoTemplate.findById(memberid, Share.class);
	}

	@Override
	public Long count(Query query, Integer size) {
		Long total = mongoTemplate.count(query, Share.class);//计算总数
		Long count = total%size==0?total/size:total/size+1;
		return count;
	}

	@Override
	public List<Share> pagingfind(Query query, Pageable pageable) {
		List<Share> list = mongoTemplate.find(query.with(pageable), Share.class);
		return list;
	}

}
