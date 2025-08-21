package com.aubotmationlab.be.dto;

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

    // 기본 크기 (1.0으로 고정하여 geometry 공유)
    private static final Double BASE_SIZE = 1.0;

    @NotNull(message = "Width scale is required")
    @Positive(message = "Width scale must be positive")
    private Double widthScale;

    @NotNull(message = "Height scale is required")
    @Positive(message = "Height scale must be positive")
    private Double heightScale;

    @NotNull(message = "Depth scale is required")
    @Positive(message = "Depth scale must be positive")
    private Double depthScale;

    @NotBlank(message = "Color is required")
    private String color;

    // 실제 크기 계산 메서드 (필요시 사용)
    public Double getActualWidth() {
        return BASE_SIZE * widthScale;
    }

    public Double getActualHeight() {
        return BASE_SIZE * heightScale;
    }

    public Double getActualDepth() {
        return BASE_SIZE * depthScale;
    }
}

