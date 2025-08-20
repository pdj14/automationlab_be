package com.aubotmationlab.be.service;

import com.aubotmationlab.be.dto.Object3DDto;
import com.aubotmationlab.be.model.Object3D;
import com.aubotmationlab.be.model.Object3D.Category;
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

    public Optional<Object3DDto> getObjectByName(String name) {
        return object3DRepository.findByName(name)
                .map(this::convertToDto);
    }

    public List<Object3DDto> getObjectsByCategory(Category category) {
        return object3DRepository.findByCategory(category)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<Object3DDto> searchObjectsByName(String name) {
        return object3DRepository.findByNameContainingIgnoreCase(name)
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

    public List<Object3DDto> getObjectsByInstancingEnabled(Boolean instancingEnabled) {
        return object3DRepository.findByInstancingEnabled(instancingEnabled)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Object3DDto createObject(Object3DDto object3DDto) {
        if (object3DRepository.existsByName(object3DDto.getName())) {
            throw new IllegalArgumentException("Object with name '" + object3DDto.getName() + "' already exists");
        }

        Object3D object3D = convertToEntity(object3DDto);
        Object3D savedObject = object3DRepository.save(object3D);
        log.info("Created new 3D object: {}", savedObject.getName());
        
        return convertToDto(savedObject);
    }

    public Optional<Object3DDto> updateObject(String id, Object3DDto object3DDto) {
        return object3DRepository.findById(id)
                .map(existingObject -> {
                    // Check if name is being changed and if the new name already exists
                    if (!existingObject.getName().equals(object3DDto.getName()) &&
                        object3DRepository.existsByName(object3DDto.getName())) {
                        throw new IllegalArgumentException("Object with name '" + object3DDto.getName() + "' already exists");
                    }

                    Object3D updatedObject = updateEntityFromDto(existingObject, object3DDto);
                    Object3D savedObject = object3DRepository.save(updatedObject);
                    log.info("Updated 3D object: {}", savedObject.getName());
                    
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
                .name(dto.getName())
                .category(dto.getCategory())
                .description(dto.getDescription())
                .glbFile(dto.getGlbFile())
                .thumbnailFile(dto.getThumbnailFile())
                .lodFile(dto.getLodFile())
                .width(dto.getWidth())
                .depth(dto.getDepth())
                .height(dto.getHeight())
                .rotation(dto.getRotation())
                .color(dto.getColor())
                .instancingEnabled(dto.getInstancingEnabled() != null ? dto.getInstancingEnabled() : false)
                .build();
    }

    private Object3DDto convertToDto(Object3D entity) {
        return Object3DDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .category(entity.getCategory())
                .description(entity.getDescription())
                .glbFile(entity.getGlbFile())
                .thumbnailFile(entity.getThumbnailFile())
                .lodFile(entity.getLodFile())
                .width(entity.getWidth())
                .depth(entity.getDepth())
                .height(entity.getHeight())
                .rotation(entity.getRotation())
                .color(entity.getColor())
                .instancingEnabled(entity.getInstancingEnabled())
                .build();
    }

    private Object3D updateEntityFromDto(Object3D existingObject, Object3DDto dto) {
        existingObject.setName(dto.getName());
        existingObject.setCategory(dto.getCategory());
        existingObject.setDescription(dto.getDescription());
        existingObject.setGlbFile(dto.getGlbFile());
        existingObject.setThumbnailFile(dto.getThumbnailFile());
        existingObject.setLodFile(dto.getLodFile());
        existingObject.setWidth(dto.getWidth());
        existingObject.setDepth(dto.getDepth());
        existingObject.setHeight(dto.getHeight());
        existingObject.setRotation(dto.getRotation());
        existingObject.setColor(dto.getColor());
        if (dto.getInstancingEnabled() != null) {
            existingObject.setInstancingEnabled(dto.getInstancingEnabled());
        }
        return existingObject;
    }
}
