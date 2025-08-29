package com.aubotmationlab.be.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Document(collection = "object3d_templates")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Object3DTemplate {

    @Id
    private String id;

    @NotBlank(message = "Template name is required")
    @Indexed(unique = true)
    private String name;

    @NotNull(message = "Category is required")
    private Category category;

    private String description;

    private String glbFile;

    private String thumbnailFile;

    private String lodFile;

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

    @Builder.Default
    private Boolean instancingEnabled = true;

    public enum Category {
        ROBOT,
        EQUIPMENT,
        APPLIANCES,
        AV
    }
}
