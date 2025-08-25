package com.aubotmationlab.be.service;

import com.aubotmationlab.be.dto.Object3DTemplateDto;
import com.aubotmationlab.be.model.Object3DTemplate;
import com.aubotmationlab.be.repository.Object3DTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Object3DTemplateService {

    private final Object3DTemplateRepository object3DTemplateRepository;

    public List<Object3DTemplateDto> getAllTemplates() {
        return object3DTemplateRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<Object3DTemplateDto> getTemplatesByCategory(Object3DTemplate.Category category) {
        return object3DTemplateRepository.findByCategory(category)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<Object3DTemplateDto> searchTemplatesByName(String name) {
        return object3DTemplateRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<Object3DTemplateDto> searchTemplatesByCategoryAndName(Object3DTemplate.Category category, String name) {
        return object3DTemplateRepository.findByCategoryAndNameContainingIgnoreCase(category, name)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Object3DTemplateDto getTemplateById(String id) {
        Object3DTemplate template = object3DTemplateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found with id: " + id));
        return convertToDto(template);
    }

    public Object3DTemplateDto createTemplate(Object3DTemplateDto templateDto) {
        Object3DTemplate template = convertToEntity(templateDto);
        Object3DTemplate savedTemplate = object3DTemplateRepository.save(template);
        return convertToDto(savedTemplate);
    }

    public Object3DTemplateDto updateTemplate(String id, Object3DTemplateDto templateDto) {
        if (!object3DTemplateRepository.existsById(id)) {
            throw new RuntimeException("Template not found with id: " + id);
        }
        
        Object3DTemplate template = convertToEntity(templateDto);
        template.setId(id);
        Object3DTemplate savedTemplate = object3DTemplateRepository.save(template);
        return convertToDto(savedTemplate);
    }

    public void deleteTemplate(String id) {
        if (!object3DTemplateRepository.existsById(id)) {
            throw new RuntimeException("Template not found with id: " + id);
        }
        object3DTemplateRepository.deleteById(id);
    }

    private Object3DTemplateDto convertToDto(Object3DTemplate template) {
        return Object3DTemplateDto.builder()
                .id(template.getId())
                .name(template.getName())
                .category(template.getCategory())
                .description(template.getDescription())
                .glbFile(template.getGlbFile())
                .thumbnailFile(template.getThumbnailFile())
                .lodFile(template.getLodFile())
                .width(template.getWidth())
                .depth(template.getDepth())
                .height(template.getHeight())
                .color(template.getColor())
                .instancingEnabled(template.getInstancingEnabled())
                .build();
    }

    private Object3DTemplate convertToEntity(Object3DTemplateDto dto) {
        return Object3DTemplate.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .description(dto.getDescription())
                .glbFile(dto.getGlbFile())
                .thumbnailFile(dto.getThumbnailFile())
                .lodFile(dto.getLodFile())
                .width(dto.getWidth())
                .depth(dto.getDepth())
                .height(dto.getHeight())
                .color(dto.getColor())
                .instancingEnabled(dto.getInstancingEnabled())
                .build();
    }
}
