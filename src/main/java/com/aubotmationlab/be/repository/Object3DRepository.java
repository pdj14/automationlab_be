package com.aubotmationlab.be.repository;

import com.aubotmationlab.be.model.Object3D;
import com.aubotmationlab.be.model.Object3D.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Object3DRepository extends MongoRepository<Object3D, String> {

    List<Object3D> findByCategory(Category category);

    @Query("{'width': {$gte: ?0, $lte: ?1}, 'height': {$gte: ?2, $lte: ?3}, 'depth': {$gte: ?4, $lte: ?5}}")
    List<Object3D> findByDimensionsRange(Double minWidth, Double maxWidth, 
                                       Double minHeight, Double maxHeight, 
                                       Double minDepth, Double maxDepth);

    List<Object3D> findByTemplateName(String templateName);

    boolean existsByName(String name);
}
