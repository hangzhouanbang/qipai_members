package com.anbang.qipai.members.plan.dao;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.anbang.qipai.members.plan.domain.Member;

public interface MemberDao {

	Member findMember(Query query);

	void updateMember(Query query, Update update);
}
