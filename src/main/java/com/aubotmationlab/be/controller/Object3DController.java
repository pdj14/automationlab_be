package com.aubotmationlab.be.controller;

import com.aubotmationlab.be.dto.Object3DDto;
import com.aubotmationlab.be.model.Object3D.Category;
import com.aubotmationlab.be.service.Object3DService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/objects")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowCredentials = "false")
public class Object3DController {

    private final Object3DService object3DService;

    @GetMapping
    public ResponseEntity<List<Object3DDto>> getAllObjects() {
        List<Object3DDto> objects = object3DService.getAllObjects();
        return ResponseEntity.ok(objects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object3DDto> getObjectById(@PathVariable String id) {
        Optional<Object3DDto> object = object3DService.getObjectById(id);
        return object.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @GetMapping("/category/{category}")
    public ResponseEntity<List<Object3DDto>> getObjectsByCategory(@PathVariable Category category) {
        List<Object3DDto> objects = object3DService.getObjectsByCategory(category);
        return ResponseEntity.ok(objects);
    }

    @GetMapping("/template/{templateName}")
    public ResponseEntity<List<Object3DDto>> getObjectsByTemplateName(@PathVariable String templateName) {
        List<Object3DDto> objects = object3DService.getObjectsByTemplateName(templateName);
        return ResponseEntity.ok(objects);
    }

    @GetMapping("/dimensions")
    public ResponseEntity<List<Object3DDto>> getObjectsByDimensionsRange(
            @RequestParam(required = false) Double minWidth,
            @RequestParam(required = false) Double maxWidth,
            @RequestParam(required = false) Double minHeight,
            @RequestParam(required = false) Double maxHeight,
            @RequestParam(required = false) Double minDepth,
            @RequestParam(required = false) Double maxDepth) {
        
        // Set default values if parameters are null
        minWidth = minWidth != null ? minWidth : 0.0;
        maxWidth = maxWidth != null ? maxWidth : Double.MAX_VALUE;
        minHeight = minHeight != null ? minHeight : 0.0;
        maxHeight = maxHeight != null ? maxHeight : Double.MAX_VALUE;
        minDepth = minDepth != null ? minDepth : 0.0;
        maxDepth = maxDepth != null ? maxDepth : Double.MAX_VALUE;

        List<Object3DDto> objects = object3DService.getObjectsByDimensionsRange(
                minWidth, maxWidth, minHeight, maxHeight, minDepth, maxDepth);
        return ResponseEntity.ok(objects);
    }



    @PostMapping
    public ResponseEntity<Object3DDto> createObject(@RequestBody Object3DDto object3DDto) {
        try {
            Object3DDto createdObject = object3DService.createObject(object3DDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdObject);
        } catch (IllegalArgumentException e) {
            log.error("Error creating object: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Object3DDto>> createObjects(@RequestBody List<Object3DDto> object3DDtos) {
        try {
            List<Object3DDto> createdObjects = object3DService.createObjects(object3DDtos);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdObjects);
        } catch (IllegalArgumentException e) {
            log.error("Error creating objects: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object3DDto> updateObject(@PathVariable String id, 
                                                  @RequestBody Object3DDto object3DDto) {
        try {
            Optional<Object3DDto> updatedObject = object3DService.updateObject(id, object3DDto);
            return updatedObject.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            log.error("Error updating object: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteObject(@PathVariable String id) {
        boolean deleted = object3DService.deleteObject(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/categories")
    public ResponseEntity<Category[]> getAvailableCategories() {
        return ResponseEntity.ok(Category.values());
    }

    @PostMapping("/from-template/{templateName}")
    public ResponseEntity<Object3DDto> createObjectFromTemplate(
            @PathVariable String templateName,
            @RequestBody Object3DDto object3DDto) {
        try {
            Object3DDto createdObject = object3DService.createObjectFromTemplate(templateName, object3DDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdObject);
        } catch (Exception e) {
            log.error("Error creating object from template: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


}
