package com.automationlab.be.repository;

import com.automationlab.be.model.Object3D;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Object3DRepository extends MongoRepository<Object3D, String> {


    List<Object3D> findByTemplateName(String templateName);

    boolean existsByName(String name);
}
