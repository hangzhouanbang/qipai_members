package com.anbang.qipai.members.plan.dao;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

import com.anbang.qipai.members.plan.domain.Share;

public interface ShareDao {
	
	public void Shareupdatecount(Share share);
	
	public Share findShare(String memberid);
	
	public Long count(Query query,Integer size);
	
	public List<Share> pagingfind(Query query,Pageable pageable);

}
