package com.aubotmationlab.be.controller;

import com.aubotmationlab.be.dto.WallDto;
import com.aubotmationlab.be.model.Wall;
import com.aubotmationlab.be.service.WallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/walls")
@RequiredArgsConstructor
public class WallController {

    private final WallService wallService;

    @GetMapping
    public ResponseEntity<List<Wall>> getAllWalls() {
        List<Wall> walls = wallService.getAllWalls();
        return ResponseEntity.ok(walls);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wall> getWallById(@PathVariable String id) {
        Optional<Wall> wall = wallService.getWallById(id);
        return wall.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Wall> createWall(@RequestBody WallDto wallDto) {
        Wall createdWall = wallService.createWall(wallDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWall);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Wall>> createWalls(@RequestBody List<WallDto> wallDtos) {
        List<Wall> createdWalls = wallService.createWalls(wallDtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWalls);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Wall> updateWall(@PathVariable String id, @RequestBody WallDto wallDto) {
        Optional<Wall> updatedWall = wallService.updateWall(id, wallDto);
        return updatedWall.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWall(@PathVariable String id) {
        boolean deleted = wallService.deleteWall(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
