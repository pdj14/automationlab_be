package com.automationlab.be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.automationlab.be.model.Object3DTemplate.Category;
import com.automationlab.be.validation.UniqueTemplateName;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Object3DTemplateDto {

    private String id;

    @NotBlank(message = "Template name is required")
    @UniqueTemplateName(message = "Template name must be unique")
    private String name;

    @NotNull(message = "Category is required")
    private Category category;

    private String description;

    // ?åÏùº ?ÖÎ°ú?úÏö© ?ÑÎìú??(MultipartFile)
    private MultipartFile glbFile;
    private MultipartFile thumbnailFile;
    private MultipartFile lodFile;

    // ?åÏùº Í≤ΩÎ°ú ?ÑÎìú??(String) - DB?êÏÑú Ï°∞Ìöå ???¨Ïö©
    private String glbFilePath;
    private String thumbnailFilePath;
    private String lodFilePath;

    @NotNull(message = "Width is required")
    @Positive(message = "Width must be positive")
    private Double width;

    @NotNull(message = "Depth is required")
    @Positive(message = "Depth must be positive")
    private Double depth;

    @NotNull(message = "Height is required")
    @Positive(message = "Height must be positive")
    private Double height;

    private String color;

    private Boolean instancingEnabled;
}
