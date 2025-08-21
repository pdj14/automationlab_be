package com.aubotmationlab.be.controller;

import com.aubotmationlab.be.dto.BoxDto;
import com.aubotmationlab.be.model.Box;
import com.aubotmationlab.be.service.BoxService;
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
public class BoxController {

    private final BoxService boxService;

    @GetMapping
    public ResponseEntity<List<Box>> getAllBoxes() {
        List<Box> boxes = boxService.getAllBoxes();
        return ResponseEntity.ok(boxes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Box> getBoxById(@PathVariable String id) {
        Optional<Box> box = boxService.getBoxById(id);
        return box.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Box> createBox(@Valid @RequestBody BoxDto boxDto) {
        Box createdBox = boxService.createBox(boxDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBox);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Box> updateBox(@PathVariable String id, @Valid @RequestBody BoxDto boxDto) {
        Optional<Box> updatedBox = boxService.updateBox(id, boxDto);
        return updatedBox.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBox(@PathVariable String id) {
        boolean deleted = boxService.deleteBox(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

