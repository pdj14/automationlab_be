package com.aubotmationlab.be.controller;

import com.aubotmationlab.be.dto.BoxDto;
import com.aubotmationlab.be.model.Box;
import com.aubotmationlab.be.service.BoxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/boxes")
@RequiredArgsConstructor
@Tag(name = "Box Management", description = "APIs for managing 3D boxes with instancing optimization")
public class BoxController {

    private final BoxService boxService;

    @GetMapping
    @Operation(summary = "Get all boxes", description = "Retrieve a list of all 3D boxes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved boxes",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Box.class)))
    })
    public ResponseEntity<List<Box>> getAllBoxes() {
        List<Box> boxes = boxService.getAllBoxes();
        return ResponseEntity.ok(boxes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get box by ID", description = "Retrieve a specific 3D box by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved box",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Box.class))),
            @ApiResponse(responseCode = "404", description = "Box not found")
    })
    public ResponseEntity<Box> getBoxById(
            @Parameter(description = "ID of the box to retrieve") @PathVariable String id) {
        Optional<Box> box = boxService.getBoxById(id);
        return box.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new box", description = "Create a new 3D box with optimized instancing support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Box created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Box.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Box> createBox(
            @Parameter(description = "Box data to create") @RequestBody BoxDto boxDto) {
        Box createdBox = boxService.createBox(boxDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBox);
    }

    @PostMapping("/bulk")
    @Operation(summary = "Create multiple boxes", description = "Create multiple 3D boxes with optimized instancing support")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Boxes created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Box.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<List<Box>> createBoxes(
            @Parameter(description = "List of box data to create") @RequestBody List<BoxDto> boxDtos) {
        List<Box> createdBoxes = boxService.createBoxes(boxDtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBoxes);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing box", description = "Update a 3D box by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Box updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Box.class))),
            @ApiResponse(responseCode = "404", description = "Box not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Box> updateBox(
            @Parameter(description = "ID of the box to update") @PathVariable String id,
            @Parameter(description = "Updated box data") @RequestBody BoxDto boxDto) {
        Optional<Box> updatedBox = boxService.updateBox(id, boxDto);
        return updatedBox.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a box", description = "Delete a 3D box by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Box deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Box not found")
    })
    public ResponseEntity<Void> deleteBox(
            @Parameter(description = "ID of the box to delete") @PathVariable String id) {
        boolean deleted = boxService.deleteBox(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
