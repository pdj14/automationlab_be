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

    // Calculate wall length (for processing positioning)
    public Double getLength() {
        double dx = endX - startX;
        double dy = endY - startY;
        return Math.sqrt(dx * dx + dy * dy);
    }

    // Calculate wall rotation angle (in radians)
    public Double getDegrees() {
        return Math.atan2(endY - startY, endX - startX);
    }

    // Wall center X coordinate
    public Double getCenterX() {
        return (startX + endX) / 2.0;
    }

    // Wall center Y coordinate
    public Double getCenterY() {
        return (startY + endY) / 2.0;
    }
}