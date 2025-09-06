package com.automationlab.be.repository;

import com.automationlab.be.model.Zone;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZoneRepository extends MongoRepository<Zone, String> {
    
    // Query zones by color (for processing grouping)
    List<Zone> findByColor(String color);
    
    // Query zone count by color
    long countByColor(String color);
}