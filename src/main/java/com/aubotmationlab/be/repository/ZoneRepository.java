package com.automationlab.be.repository;

import com.automationlab.be.model.Zone;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZoneRepository extends MongoRepository<Zone, String> {
    
    // ?‰ìƒë³?Zone ì¡°íšŒ (?¸ìŠ¤?´ì‹± ê·¸ë£¹?”ìš©)
    List<Zone> findByColor(String color);
    
    // ?‰ìƒë³?Zone ê°œìˆ˜ ì¡°íšŒ
    long countByColor(String color);
}
