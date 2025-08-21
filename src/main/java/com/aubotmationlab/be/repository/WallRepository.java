package com.aubotmationlab.be.repository;

import com.aubotmationlab.be.model.Wall;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WallRepository extends MongoRepository<Wall, String> {
}
