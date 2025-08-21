package com.aubotmationlab.be.controller;

import com.aubotmationlab.be.dto.ZoneDto;
import com.aubotmationlab.be.model.Zone;
import com.aubotmationlab.be.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    @GetMapping
    public ResponseEntity<List<Zone>> getAllZones() {
        List<Zone> zones = zoneService.getAllZones();
        return ResponseEntity.ok(zones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Zone> getZoneById(@PathVariable String id) {
        Optional<Zone> zone = zoneService.getZoneById(id);
        return zone.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Zone> createZone(@RequestBody ZoneDto zoneDto) {
        Zone createdZone = zoneService.createZone(zoneDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdZone);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Zone>> createZones(@RequestBody List<ZoneDto> zoneDtos) {
        List<Zone> createdZones = zoneService.createZones(zoneDtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdZones);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Zone> updateZone(@PathVariable String id, @RequestBody ZoneDto zoneDto) {
        Optional<Zone> updatedZone = zoneService.updateZone(id, zoneDto);
        return updatedZone.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZone(@PathVariable String id) {
        boolean deleted = zoneService.deleteZone(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // 인스턴싱을 위한 색상별 Zone 그룹화
    @GetMapping("/color-groups")
    public ResponseEntity<Map<String, List<Zone>>> getZonesByColorGroup() {
        Map<String, List<Zone>> zonesByColor = zoneService.getZonesByColorGroup();
        return ResponseEntity.ok(zonesByColor);
    }

    // 특정 색상의 Zone들 조회
    @GetMapping("/color/{color}")
    public ResponseEntity<List<Zone>> getZonesByColor(@PathVariable String color) {
        List<Zone> zones = zoneService.getZonesByColor(color);
        return ResponseEntity.ok(zones);
    }

    // 색상별 Zone 개수 조회
    @GetMapping("/color-counts")
    public ResponseEntity<Map<String, Long>> getZoneCountByColor() {
        Map<String, Long> colorCounts = zoneService.getZoneCountByColor();
        return ResponseEntity.ok(colorCounts);
    }
}
