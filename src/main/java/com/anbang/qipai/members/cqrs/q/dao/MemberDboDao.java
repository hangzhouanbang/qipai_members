package com.anbang.qipai.members.cqrs.q.dao;

import com.anbang.qipai.members.cqrs.q.dbo.MemberDbo;

public interface MemberDboDao {

	void save(MemberDbo memberDbo);

	void update(String memberId, String nickname, String headimgurl);

	void updateLoginTime(String memberId, long loginTime);

	void updateGold(String memberId, int gold);

	void updateScore(String memberId, int score);

	MemberDbo findById(String id);

}
