package com.aubotmationlab.be.repository;

import com.aubotmationlab.be.model.Object3DTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Object3DTemplateRepository extends MongoRepository<Object3DTemplate, String> {
    
    List<Object3DTemplate> findByCategory(Object3DTemplate.Category category);
    
    List<Object3DTemplate> findByNameContainingIgnoreCase(String name);
    
    List<Object3DTemplate> findByCategoryAndNameContainingIgnoreCase(Object3DTemplate.Category category, String name);
    
    boolean existsByName(String name);
    
    Optional<Object3DTemplate> findByName(String name);
}
