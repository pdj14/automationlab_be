package com.aubotmationlab.be.repository;

import com.aubotmationlab.be.model.Zone;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZoneRepository extends MongoRepository<Zone, String> {
    
    // 색상별 Zone 조회 (인스턴싱 그룹화용)
    List<Zone> findByColor(String color);
    
    // 색상별 Zone 개수 조회
    long countByColor(String color);
}
