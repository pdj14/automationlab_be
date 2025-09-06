package com.automationlab.be.service;

import com.automationlab.be.dto.Object3DDto;
import com.automationlab.be.dto.Object3DTemplateDto;
import com.automationlab.be.model.Object3D;
import com.automationlab.be.repository.Object3DRepository;
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

    public List<Object3DDto> getObjectsByTemplateName(String templateName) {
        return object3DRepository.findByTemplateName(templateName)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Object3DDto createObject(Object3DDto object3DDto) {
        // Get template information
        Object3DTemplateDto template = object3DTemplateService.getTemplateByName(object3DDto.getTemplateName());
        
        // Create Object3D (using template information as default values)
        Object3D object3D = Object3D.builder()
                .description(object3DDto.getDescription() != null ? object3DDto.getDescription() : template.getDescription())
                .degrees(object3DDto.getDegrees())
                .x(object3DDto.getX())
                .y(object3DDto.getY())
                .templateName(object3DDto.getTemplateName())
                .build();

        Object3D savedObject = object3DRepository.save(object3D);
        log.info("Created new 3D object: {} from template: {}", savedObject.getId(), object3DDto.getTemplateName());
        
        return convertToDto(savedObject);
    }

    public Object3DDto createObjectFromTemplate(String templateName, Object3DDto object3DDto) {
        // Set templateName and call createObject
        object3DDto.setTemplateName(templateName);
        return createObject(object3DDto);
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
                .description(dto.getDescription())
                .degrees(dto.getDegrees())
                .x(dto.getX())
                .y(dto.getY())
                .templateName(dto.getTemplateName())
                .build();
    }

    private Object3DDto convertToDto(Object3D entity) {
        return Object3DDto.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .degrees(entity.getDegrees())
                .x(entity.getX())
                .y(entity.getY())
                .templateName(entity.getTemplateName())
                .build();
    }

    private Object3D updateEntityFromDto(Object3D existingObject, Object3DDto dto) {
        existingObject.setDescription(dto.getDescription());
        existingObject.setDegrees(dto.getDegrees());
        existingObject.setX(dto.getX());
        existingObject.setY(dto.getY());
        existingObject.setTemplateName(dto.getTemplateName());
        return existingObject;
    }

}