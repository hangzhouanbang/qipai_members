package com.anbang.qipai.members.plan.dao;

import java.util.List;

import com.anbang.qipai.members.plan.bean.ScoreProductRecord;

public interface ScoreProductRecordDao {

	void save(ScoreProductRecord record);

	ScoreProductRecord findById(String id);

	void updateStatusById(String id, String status);

	void updateDeliverTimeById(String id, long deliverTime);

	long countByMemberIdAndStatus(String memberId, String status);

	List<ScoreProductRecord> findByMemberIdAndStatus(int page, int size, String memberId, String status);
}
