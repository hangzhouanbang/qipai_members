package com.anbang.qipai.members.cqrs.q.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.members.cqrs.q.dbo.ScoreShopProductDbo;

public interface ScoreShopProductDboRepository extends MongoRepository<ScoreShopProductDbo, String> {

}
