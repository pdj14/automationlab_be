package com.aubotmationlab.be.controller;

import com.aubotmationlab.be.dto.Object3DDto;
import com.aubotmationlab.be.dto.Object3DTemplateDto;
import com.aubotmationlab.be.model.Object3D.Category;
import com.aubotmationlab.be.model.Object3DTemplate;
import com.aubotmationlab.be.service.Object3DService;
import com.aubotmationlab.be.service.Object3DTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/objects")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class Object3DController {

    private final Object3DService object3DService;
    private final Object3DTemplateService object3DTemplateService;

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

    @GetMapping("/name/{name}")
    public ResponseEntity<Object3DDto> getObjectByName(@PathVariable String name) {
        Optional<Object3DDto> object = object3DService.getObjectByName(name);
        return object.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Object3DDto>> getObjectsByCategory(@PathVariable Category category) {
        List<Object3DDto> objects = object3DService.getObjectsByCategory(category);
        return ResponseEntity.ok(objects);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Object3DDto>> searchObjectsByName(@RequestParam String name) {
        List<Object3DDto> objects = object3DService.searchObjectsByName(name);
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

    @GetMapping("/instancing")
    public ResponseEntity<List<Object3DDto>> getObjectsByInstancingEnabled(
            @RequestParam Boolean instancingEnabled) {
        List<Object3DDto> objects = object3DService.getObjectsByInstancingEnabled(instancingEnabled);
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

    @PostMapping("/from-template/{templateId}")
    public ResponseEntity<Object3DDto> createObjectFromTemplate(
            @PathVariable String templateId,
            @RequestBody Object3DDto object3DDto) {
        try {
            // Get template information
            Object3DTemplateDto template = object3DTemplateService.getTemplateById(templateId);
            
            // Merge template data with provided object data
            Object3DDto mergedObject = Object3DDto.builder()
                    .name(object3DDto.getName() != null ? object3DDto.getName() : template.getName())
                    .category(object3DDto.getCategory() != null ? object3DDto.getCategory() : convertCategory(template.getCategory()))
                    .description(object3DDto.getDescription() != null ? object3DDto.getDescription() : template.getDescription())
                    .glbFile(template.getGlbFile())
                    .thumbnailFile(template.getThumbnailFile())
                    .lodFile(template.getLodFile())
                    .width(template.getWidth())
                    .depth(template.getDepth())
                    .height(template.getHeight())
                    .color(object3DDto.getColor() != null ? object3DDto.getColor() : template.getColor())
                    .instancingEnabled(object3DDto.getInstancingEnabled() != null ? object3DDto.getInstancingEnabled() : template.getInstancingEnabled())
                    .x(object3DDto.getX())
                    .y(object3DDto.getY())
                    .rotation(object3DDto.getRotation())
                    .build();
            
            Object3DDto createdObject = object3DService.createObject(mergedObject);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdObject);
        } catch (Exception e) {
            log.error("Error creating object from template: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    private Category convertCategory(Object3DTemplate.Category templateCategory) {
        switch (templateCategory) {
            case ROBOT:
                return Category.ROBOT;
            case EQUIPMENT:
                return Category.EQUIPMENT;
            case APPLIANCES:
                return Category.APPLIANCES;
            case AV:
                return Category.AV;
            default:
                return Category.EQUIPMENT; // Default fallback
        }
    }
}
