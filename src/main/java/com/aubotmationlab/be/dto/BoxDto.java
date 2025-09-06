package com.automationlab.be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoxDto {

    private String id;

    @NotNull(message = "X coordinate is required")
    private Double x;

    @NotNull(message = "Y coordinate is required")
    private Double y;

    // 기본 ?�기 (1.0?�로 고정?�여 geometry 공유)
    private static final Double BASE_SIZE = 1.0;

    @NotNull(message = "Width is required")
    @Positive(message = "Width must be positive")
    private Double width;

    @NotNull(message = "Height is required")
    @Positive(message = "Height must be positive")
    private Double height;

    @NotNull(message = "Depth is required")
    @Positive(message = "Depth must be positive")
    private Double depth;

    @NotBlank(message = "Color is required")
    private String color;

    // ?�제 ?�기 계산 메서??(?�요???�용)
    public Double getActualWidth() {
        return BASE_SIZE * width;
    }

    public Double getActualHeight() {
        return BASE_SIZE * height;
    }

    public Double getActualDepth() {
        return BASE_SIZE * depth;
    }
}

