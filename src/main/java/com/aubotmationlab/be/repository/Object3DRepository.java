package com.aubotmationlab.be.repository;

import com.aubotmationlab.be.model.Object3D;
import com.aubotmationlab.be.model.Object3D.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Object3DRepository extends MongoRepository<Object3D, String> {

    Optional<Object3D> findByName(String name);

    List<Object3D> findByCategory(Category category);

    List<Object3D> findByNameContainingIgnoreCase(String name);

    @Query("{'width': {$gte: ?0, $lte: ?1}, 'height': {$gte: ?2, $lte: ?3}, 'depth': {$gte: ?4, $lte: ?5}}")
    List<Object3D> findByDimensionsRange(Double minWidth, Double maxWidth, 
                                       Double minHeight, Double maxHeight, 
                                       Double minDepth, Double maxDepth);

    List<Object3D> findByInstancingEnabled(Boolean instancingEnabled);

    boolean existsByName(String name);
}
