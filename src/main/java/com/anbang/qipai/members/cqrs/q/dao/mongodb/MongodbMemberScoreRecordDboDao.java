package com.anbang.qipai.members.cqrs.q.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.cqrs.q.dao.MemberScoreRecordDboDao;
import com.anbang.qipai.members.cqrs.q.dao.mongodb.repository.MemberScoreRecordDboRepository;
import com.anbang.qipai.members.cqrs.q.dbo.MemberScoreRecordDbo;

@Component
public class MongodbMemberScoreRecordDboDao implements MemberScoreRecordDboDao {
	@Autowired
	private MemberScoreRecordDboRepository repository;

	@Override
	public void save(MemberScoreRecordDbo dbo) {
		repository.save(dbo);
	}

	@Override
	public long getCount() {
		return repository.count();
	}

	@Override
	public List<MemberScoreRecordDbo> findMemberScoreRecords(String memberId, PageRequest pageRequest) {
		return repository.findByMemberId(memberId, pageRequest);
	}

}
