package com.anbang.qipai.members.plan.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.members.plan.bean.MemberClubCard;
import com.anbang.qipai.members.plan.dao.ClubCardDao;
import com.mongodb.WriteResult;

@Component
public class MongodbClubCardDao implements ClubCardDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<MemberClubCard> findAllClubCard() {
		return mongoTemplate.findAll(MemberClubCard.class);
	}

	@Override
	public MemberClubCard getClubCardById(String clubCardId) {
		Query query = new Query(Criteria.where("id").is(clubCardId));
		return mongoTemplate.findOne(query, MemberClubCard.class);
	}

	@Override
	public void addClubCard(MemberClubCard clubCard) {
		mongoTemplate.insert(clubCard);
	}

	@Override
	public boolean deleteClubCardByIds(String[] clubCardIds) {
		Object[] ids = clubCardIds;
		Query query = new Query(Criteria.where("id").in(ids));
		WriteResult writeResult = mongoTemplate.remove(query, MemberClubCard.class);
		return writeResult.getN() <= clubCardIds.length;
	}

	@Override
	public boolean updateClubCard(MemberClubCard clubCard) {
		Query query = new Query(Criteria.where("id").is(clubCard.getId()));
		Update update = new Update();
		update.set("name", clubCard.getName());
		update.set("price", clubCard.getPrice());
		update.set("gold", clubCard.getGold());
		update.set("score", clubCard.getScore());
		update.set("time", clubCard.getTime());
		WriteResult writeResult = mongoTemplate.updateFirst(query, update, MemberClubCard.class);
		return writeResult.getN() > 0;
	}

}
