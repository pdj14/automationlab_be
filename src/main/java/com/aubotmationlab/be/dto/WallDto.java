package com.automationlab.be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WallDto {

    private String id;

    @NotNull(message = "Start X coordinate is required")
    private Double startX;

    @NotNull(message = "Start Y coordinate is required")
    private Double startY;

    @NotNull(message = "End X coordinate is required")
    private Double endX;

    @NotNull(message = "End Y coordinate is required")
    private Double endY;

    @NotNull(message = "Is glass flag is required")
    private Boolean isGlass;

    // 벽의 길이 계산 (?�스?�싱 ???��??�로 ?�용)
    public Double getLength() {
        double dx = endX - startX;
        double dy = endY - startY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    // 벽의 ?�전 각도 계산 (?�디??
    public Double getDegrees() {
        return Math.atan2(endY - startY, endX - startX);
    }

    // 벽의 중점 X 좌표
    public Double getCenterX() {
        return (startX + endX) / 2.0;
    }

    // 벽의 중점 Y 좌표
    public Double getCenterY() {
        return (startY + endY) / 2.0;
    }
}
