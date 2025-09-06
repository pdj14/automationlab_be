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

    // Color group for processing (for internal use)
    public String getColorGroup() {
        return this.color != null ? this.color.toLowerCase() : "default";
    }

    // Zone center X coordinate (for processing positioning)
    public Double getCenterX() {
        return x + (width / 2.0);
    }

    // Zone center Y coordinate (for processing positioning)
    public Double getCenterY() {
        return y + (height / 2.0);
    }
}