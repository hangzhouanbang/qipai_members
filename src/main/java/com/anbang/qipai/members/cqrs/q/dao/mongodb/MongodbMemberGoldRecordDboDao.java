package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.q.dao.MemberGoldRecordDboDao;
import com.anbang.qipai.members.cqrs.q.dao.mongodb.repository.MemberGoldRecordDboRepository;
import com.anbang.qipai.members.cqrs.q.dbo.MemberGoldRecordDbo;

@Component
public class MongodbMemberGoldRecordDboDao implements MemberGoldRecordDboDao {

	@Autowired
	private MemberGoldRecordDboRepository repository;

	@Override
	public void save(MemberGoldRecordDbo dbo) {
		repository.save(dbo);
	}

	@Override
	public long getCount() {
		return repository.count();
	}

	@Override
	public List<MemberGoldRecordDbo> findMemberGoldRecords(String memberId, PageRequest pageRequest) {
		return repository.findByMemberId(memberId, pageRequest);
	}

}
