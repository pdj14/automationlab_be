package com.aubotmationlab.be.controller;

import com.aubotmationlab.be.dto.Object3DTemplateDto;
import com.aubotmationlab.be.model.Object3DTemplate;
import com.aubotmationlab.be.service.Object3DTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/object3d-templates")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class Object3DTemplateController {

    private final Object3DTemplateService object3DTemplateService;

    @GetMapping
    public ResponseEntity<List<Object3DTemplateDto>> getAllTemplates() {
        List<Object3DTemplateDto> templates = object3DTemplateService.getAllTemplates();
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object3DTemplateDto> getTemplateById(@PathVariable String id) {
        Object3DTemplateDto template = object3DTemplateService.getTemplateById(id);
        return ResponseEntity.ok(template);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Object3DTemplateDto>> getTemplatesByCategory(@PathVariable Object3DTemplate.Category category) {
        List<Object3DTemplateDto> templates = object3DTemplateService.getTemplatesByCategory(category);
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Object3DTemplateDto>> searchTemplatesByName(@RequestParam String name) {
        List<Object3DTemplateDto> templates = object3DTemplateService.searchTemplatesByName(name);
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/search/category")
    public ResponseEntity<List<Object3DTemplateDto>> searchTemplatesByCategoryAndName(
            @RequestParam Object3DTemplate.Category category,
            @RequestParam String name) {
        List<Object3DTemplateDto> templates = object3DTemplateService.searchTemplatesByCategoryAndName(category, name);
        return ResponseEntity.ok(templates);
    }

    @PostMapping
    public ResponseEntity<Object3DTemplateDto> createTemplate(@Valid @RequestBody Object3DTemplateDto templateDto) {
        try {
            Object3DTemplateDto createdTemplate = object3DTemplateService.createTemplate(templateDto);
            return ResponseEntity.ok(createdTemplate);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<Object3DTemplateDto> createTemplateWithFiles(
            @RequestParam("name") String name,
            @RequestParam("category") Object3DTemplate.Category category,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "glbFile", required = false) MultipartFile glbFile,
            @RequestParam(value = "thumbnailFile", required = false) MultipartFile thumbnailFile,
            @RequestParam(value = "lodFile", required = false) MultipartFile lodFile,
            @RequestParam("width") Double width,
            @RequestParam("depth") Double depth,
            @RequestParam("height") Double height,
            @RequestParam(value = "color", required = false) String color,
            @RequestParam(value = "instancingEnabled", defaultValue = "false") Boolean instancingEnabled) {
        
        Object3DTemplateDto createDto = Object3DTemplateDto.builder()
                .name(name)
                .category(category)
                .description(description)
                .glbFile(glbFile)
                .thumbnailFile(thumbnailFile)
                .lodFile(lodFile)
                .width(width)
                .depth(depth)
                .height(height)
                .color(color)
                .instancingEnabled(instancingEnabled)
                .build();
        
        try {
            Object3DTemplateDto createdTemplate = object3DTemplateService.createTemplateWithFiles(createDto);
            return ResponseEntity.ok(createdTemplate);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object3DTemplateDto> updateTemplate(
            @PathVariable String id,
            @Valid @RequestBody Object3DTemplateDto templateDto) {
        try {
            Object3DTemplateDto updatedTemplate = object3DTemplateService.updateTemplate(id, templateDto);
            return ResponseEntity.ok(updatedTemplate);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable String id) {
        object3DTemplateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/files/{fileType}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String id,
            @PathVariable String fileType) {
        try {
            Object3DTemplateDto template = object3DTemplateService.getTemplateById(id);
            String filePath = null;
            String fileName = null;

            switch (fileType.toLowerCase()) {
                case "glb":
                    filePath = template.getGlbFilePath();
                    fileName = template.getName() + ".glb";
                    break;
                case "thumbnail":
                    filePath = template.getThumbnailFilePath();
                    fileName = template.getName() + "_thumbnail.jpg";
                    break;
                case "lod":
                    filePath = template.getLodFilePath();
                    fileName = template.getName() + "_lod.glb";
                    break;
                default:
                    return ResponseEntity.notFound().build();
            }

            if (filePath == null || filePath.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Path file = object3DTemplateService.getFilePath(filePath);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
