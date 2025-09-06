package com.automationlab.be.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "walls")
public class Wall {

    @Id
    private String id;

    private Double startX;
    private Double startY;
    private Double endX;
    private Double endY;
    private Boolean isGlass;
}
