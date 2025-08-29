package com.aubotmationlab.be.service;

import com.aubotmationlab.be.dto.Object3DDto;
import com.aubotmationlab.be.dto.Object3DTemplateDto;
import com.aubotmationlab.be.model.Object3D;
import com.aubotmationlab.be.model.Object3D.Category;
import com.aubotmationlab.be.model.Object3DTemplate;
import com.aubotmationlab.be.repository.Object3DRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class Object3DService {

    private final Object3DRepository object3DRepository;
    private final Object3DTemplateService object3DTemplateService;

    public List<Object3DDto> getAllObjects() {
        return object3DRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<Object3DDto> getObjectById(String id) {
        return object3DRepository.findById(id)
                .map(this::convertToDto);
    }



    public List<Object3DDto> getObjectsByCategory(Category category) {
        return object3DRepository.findByCategory(category)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<Object3DDto> getObjectsByTemplateName(String templateName) {
        return object3DRepository.findByTemplateName(templateName)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<Object3DDto> getObjectsByDimensionsRange(Double minWidth, Double maxWidth,
                                                        Double minHeight, Double maxHeight,
                                                        Double minDepth, Double maxDepth) {
        return object3DRepository.findByDimensionsRange(minWidth, maxWidth, minHeight, maxHeight, minDepth, maxDepth)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }



    public Object3DDto createObject(Object3DDto object3DDto) {
        Object3D object3D = convertToEntity(object3DDto);
        Object3D savedObject = object3DRepository.save(object3D);
        log.info("Created new 3D object: {}", savedObject.getId());
        
        return convertToDto(savedObject);
    }

    public Object3DDto createObjectFromTemplate(String templateName, Object3DDto object3DDto) {
        try {
            // 템플릿 정보 가져오기 (name으로 검색)
            Object3DTemplateDto template = object3DTemplateService.getTemplateByName(templateName);
            
            // Object3D 생성 (템플릿 정보를 기본값으로 사용)
            Object3D object3D = Object3D.builder()
                    .category(object3DDto.getCategory() != null ? object3DDto.getCategory() : convertCategory(template.getCategory()))
                    .description(object3DDto.getDescription() != null ? object3DDto.getDescription() : template.getDescription())
                    .width(object3DDto.getWidth() != null ? object3DDto.getWidth() : template.getWidth())
                    .depth(object3DDto.getDepth() != null ? object3DDto.getDepth() : template.getDepth())
                    .height(object3DDto.getHeight() != null ? object3DDto.getHeight() : template.getHeight())
                    .rotation(object3DDto.getRotation())
                    .x(object3DDto.getX())
                    .y(object3DDto.getY())
                    .color(object3DDto.getColor() != null ? object3DDto.getColor() : template.getColor())
                    .templateName(templateName)
                    .build();

            Object3D savedObject = object3DRepository.save(object3D);
            log.info("Created new 3D object from template: {} (template: {})", savedObject.getId(), templateName);
            
            return convertToDto(savedObject);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create object from template: " + e.getMessage(), e);
        }
    }

    public List<Object3DDto> createObjects(List<Object3DDto> object3DDtos) {
        List<Object3D> objects = object3DDtos.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
        
        List<Object3D> savedObjects = object3DRepository.saveAll(objects);
        log.info("Created {} new 3D objects", savedObjects.size());
        
        return savedObjects.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<Object3DDto> updateObject(String id, Object3DDto object3DDto) {
        return object3DRepository.findById(id)
                .map(existingObject -> {
                    Object3D updatedObject = updateEntityFromDto(existingObject, object3DDto);
                    Object3D savedObject = object3DRepository.save(updatedObject);
                    log.info("Updated 3D object: {}", savedObject.getId());
                    
                    return convertToDto(savedObject);
                });
    }

    public boolean deleteObject(String id) {
        if (object3DRepository.existsById(id)) {
            object3DRepository.deleteById(id);
            log.info("Deleted 3D object with id: {}", id);
            return true;
        }
        return false;
    }

    private Object3D convertToEntity(Object3DDto dto) {
        return Object3D.builder()
                .category(dto.getCategory())
                .description(dto.getDescription())
                .width(dto.getWidth())
                .depth(dto.getDepth())
                .height(dto.getHeight())
                .rotation(dto.getRotation())
                .x(dto.getX())
                .y(dto.getY())
                .color(dto.getColor())
                .templateName(dto.getTemplateName())
                .build();
    }

    private Object3DDto convertToDto(Object3D entity) {
        return Object3DDto.builder()
                .id(entity.getId())
                .category(entity.getCategory())
                .description(entity.getDescription())
                .width(entity.getWidth())
                .depth(entity.getDepth())
                .height(entity.getHeight())
                .rotation(entity.getRotation())
                .x(entity.getX())
                .y(entity.getY())
                .color(entity.getColor())
                .templateName(entity.getTemplateName())
                .build();
    }

    private Object3D updateEntityFromDto(Object3D existingObject, Object3DDto dto) {
        existingObject.setCategory(dto.getCategory());
        existingObject.setDescription(dto.getDescription());
        existingObject.setWidth(dto.getWidth());
        existingObject.setDepth(dto.getDepth());
        existingObject.setHeight(dto.getHeight());
        existingObject.setRotation(dto.getRotation());
        existingObject.setX(dto.getX());
        existingObject.setY(dto.getY());
        existingObject.setColor(dto.getColor());
        existingObject.setTemplateName(dto.getTemplateName());
        return existingObject;
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
