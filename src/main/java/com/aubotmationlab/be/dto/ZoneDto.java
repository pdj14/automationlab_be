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
public class ZoneDto {

    private String id;

    @NotNull(message = "X coordinate is required")
    private Double x;

    @NotNull(message = "Y coordinate is required")
    private Double y;

    @NotNull(message = "Width is required")
    @Positive(message = "Width must be positive")
    private Double width;

    @NotNull(message = "Height is required")
    @Positive(message = "Height must be positive")
    private Double height;

    private String name;

    private String description;

    private String color;

    // 인스턴싱을 위한 색상 그룹 식별자
    public String getColorGroup() {
        return this.color != null ? this.color.toLowerCase() : "default";
    }

    // Zone의 중점 X 좌표 (인스턴싱 시 위치로 사용)
    public Double getCenterX() {
        return x + (width / 2.0);
    }

    // Zone의 중점 Y 좌표 (인스턴싱 시 위치로 사용)
    public Double getCenterY() {
        return y + (height / 2.0);
    }
}
