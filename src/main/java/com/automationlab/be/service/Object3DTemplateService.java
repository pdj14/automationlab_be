package com.automationlab.be.service;

import com.automationlab.be.dto.Object3DTemplateDto;
import com.automationlab.be.model.Object3DTemplate;
import com.automationlab.be.repository.Object3DTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Object3DTemplateService {

    private final Object3DTemplateRepository object3DTemplateRepository;
    private final FileStorageService fileStorageService;

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

    public Object3DTemplateDto getTemplateByName(String name) {
        Object3DTemplate template = object3DTemplateRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Template not found with name: " + name));
        return convertToDto(template);
    }

    public Object3DTemplateDto createTemplate(Object3DTemplateDto templateDto) {
        // Check name duplication
        if (object3DTemplateRepository.existsByName(templateDto.getName())) {
            throw new IllegalArgumentException("Template with name '" + templateDto.getName() + "' already exists");
        }
        
        Object3DTemplate template = convertToEntity(templateDto);
        Object3DTemplate savedTemplate = object3DTemplateRepository.save(template);
        return convertToDto(savedTemplate);
    }

    public Object3DTemplateDto createTemplateWithFiles(Object3DTemplateDto templateDto) {
        try {
            // Check name duplication
            if (object3DTemplateRepository.existsByName(templateDto.getName())) {
                throw new IllegalArgumentException("Template with name '" + templateDto.getName() + "' already exists");
            }
            
            String glbFilePath = null;
            String thumbnailFilePath = null;
            String lodFilePath = null;

            // Save GLB file
            if (templateDto.getGlbFile() != null && !templateDto.getGlbFile().isEmpty()) {
                glbFilePath = fileStorageService.storeFile(templateDto.getGlbFile(), templateDto.getName(), "glb");
            }

            // Save thumbnail file
            if (templateDto.getThumbnailFile() != null && !templateDto.getThumbnailFile().isEmpty()) {
                thumbnailFilePath = fileStorageService.storeFile(templateDto.getThumbnailFile(), templateDto.getName(), "thumbnail");
            }

            // Save LOD file
            if (templateDto.getLodFile() != null && !templateDto.getLodFile().isEmpty()) {
                lodFilePath = fileStorageService.storeFile(templateDto.getLodFile(), templateDto.getName(), "lod");
            }

            // Create template
            Object3DTemplate template = Object3DTemplate.builder()
                    .name(templateDto.getName())
                    .category(templateDto.getCategory())
                    .description(templateDto.getDescription())
                    .glbFile(glbFilePath)
                    .thumbnailFile(thumbnailFilePath)
                    .lodFile(lodFilePath)
                    .width(templateDto.getWidth())
                    .depth(templateDto.getDepth())
                    .height(templateDto.getHeight())
                    .color(templateDto.getColor())
                    .instancingEnabled(templateDto.getInstancingEnabled())
                    .build();

            Object3DTemplate savedTemplate = object3DTemplateRepository.save(template);
            return convertToDto(savedTemplate);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }

    public Object3DTemplateDto updateTemplate(String id, Object3DTemplateDto templateDto) {
        Object3DTemplate existingTemplate = object3DTemplateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found with id: " + id));
        
        // Check duplication if name is changed
        if (!existingTemplate.getName().equals(templateDto.getName()) && 
            object3DTemplateRepository.existsByName(templateDto.getName())) {
            throw new IllegalArgumentException("Template with name '" + templateDto.getName() + "' already exists");
        }
        
        Object3DTemplate template = convertToEntity(templateDto);
        template.setId(id);
        Object3DTemplate savedTemplate = object3DTemplateRepository.save(template);
        return convertToDto(savedTemplate);
    }

    public void deleteTemplate(String id) {
        Object3DTemplate template = object3DTemplateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found with id: " + id));
        
        try {
            // Delete entire template folder
            fileStorageService.deleteTemplateFolder(template.getName());
            
            // Delete template from database
            object3DTemplateRepository.deleteById(id);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete template folder: " + e.getMessage(), e);
        }
    }

    private Object3DTemplateDto convertToDto(Object3DTemplate template) {
        return Object3DTemplateDto.builder()
                .id(template.getId())
                .name(template.getName())
                .category(template.getCategory())
                .description(template.getDescription())
                .glbFile(null) // Set MultipartFile to null
                .thumbnailFile(null)
                .lodFile(null)
                .glbFilePath(template.getGlbFile()) // Set file path as String
                .thumbnailFilePath(template.getThumbnailFile())
                .lodFilePath(template.getLodFile())
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
                .glbFile(dto.getGlbFilePath()) // Use file path
                .thumbnailFile(dto.getThumbnailFilePath())
                .lodFile(dto.getLodFilePath())
                .width(dto.getWidth())
                .depth(dto.getDepth())
                .height(dto.getHeight())
                .color(dto.getColor())
                .instancingEnabled(dto.getInstancingEnabled())
                .build();
    }

    public Path getFilePath(String filePath) {
        return fileStorageService.getFilePath(filePath);
    }
}