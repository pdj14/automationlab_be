package com.aubotmationlab.be.controller;

import com.aubotmationlab.be.dto.Object3DTemplateDto;
import com.aubotmationlab.be.model.Object3DTemplate;
import com.aubotmationlab.be.service.Object3DTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object3DTemplateDto> createTemplate(@RequestBody Object3DTemplateDto templateDto) {
        Object3DTemplateDto createdTemplate = object3DTemplateService.createTemplate(templateDto);
        return ResponseEntity.ok(createdTemplate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object3DTemplateDto> updateTemplate(
            @PathVariable String id,
            @RequestBody Object3DTemplateDto templateDto) {
        Object3DTemplateDto updatedTemplate = object3DTemplateService.updateTemplate(id, templateDto);
        return ResponseEntity.ok(updatedTemplate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable String id) {
        object3DTemplateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }
}
