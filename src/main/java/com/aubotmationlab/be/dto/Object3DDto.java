package com.automationlab.be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Object3DDto {

    private String id;

    private String description;

    @NotNull(message = "Degrees is required")
    @DecimalMin(value = "0.0", message = "Degrees must be between 0 and 360")
    @DecimalMax(value = "360.0", message = "Degrees must be between 0 and 360")
    private Double degrees;

    @NotNull(message = "X coordinate is required")
    private Double x;

    @NotNull(message = "Y coordinate is required")
    private Double y;

    // 템플릿 참조 필드 (필수)
    @NotNull(message = "Template name is required")
    private String templateName;
}
