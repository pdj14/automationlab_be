package com.aubotmationlab.be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.aubotmationlab.be.model.Object3D.Category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Object3DDto {

    private String id;

    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Category is required")
    private Category category;

    private String description;

    @NotNull(message = "Width is required")
    @Positive(message = "Width must be positive")
    private Double width;

    @NotNull(message = "Depth is required")
    @Positive(message = "Depth must be positive")
    private Double depth;

    @NotNull(message = "Height is required")
    @Positive(message = "Height must be positive")
    private Double height;

    @NotNull(message = "Rotation is required")
    @DecimalMin(value = "0.0", message = "Rotation must be between 0 and 360")
    @DecimalMax(value = "360.0", message = "Rotation must be between 0 and 360")
    private Double rotation;

    @NotNull(message = "X coordinate is required")
    private Double x;

    @NotNull(message = "Y coordinate is required")
    private Double y;

    private String color;

    // 템플릿 참조 필드
    private String templateName;
}
