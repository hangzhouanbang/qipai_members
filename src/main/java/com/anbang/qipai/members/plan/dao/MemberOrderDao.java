package com.anbang.qipai.members.plan.dao;

import com.anbang.qipai.members.plan.bean.MemberOrder;

public interface MemberOrderDao {

	void addMemberOrder(MemberOrder order);

	void orderFinished(String id, String transaction_id, String status, long deliveTime);

	MemberOrder findMemberOrderById(String id);

	MemberOrder findMemberOrderByPayerIdAndProductName(String payerId, String productName);
}
