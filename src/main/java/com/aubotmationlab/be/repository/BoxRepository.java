package com.aubotmationlab.be.repository;

import com.aubotmationlab.be.model.Box;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoxRepository extends MongoRepository<Box, String> {
}
