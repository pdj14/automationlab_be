package com.automationlab.be.repository;

import com.automationlab.be.model.Zone;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZoneRepository extends MongoRepository<Zone, String> {
    
    // ?�상�?Zone 조회 (?�스?�싱 그룹?�용)
    List<Zone> findByColor(String color);
    
    // ?�상�?Zone 개수 조회
    long countByColor(String color);
}
