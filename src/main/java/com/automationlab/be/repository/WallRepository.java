package com.automationlab.be.repository;

import com.automationlab.be.model.Wall;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WallRepository extends MongoRepository<Wall, String> {
}
