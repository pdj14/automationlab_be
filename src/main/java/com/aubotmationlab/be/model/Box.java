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
@Document(collection = "boxes")
public class Box {

    @Id
    private String id;

    private Double x;
    private Double y;
    private Double width;
    private Double height;
    private Double depth;
    private String color;
}

