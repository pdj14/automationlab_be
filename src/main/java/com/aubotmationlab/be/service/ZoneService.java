package com.automationlab.be.service;

import com.automationlab.be.dto.ZoneDto;
import com.automationlab.be.model.Zone;
import com.automationlab.be.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepository zoneRepository;

    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }

    public Optional<Zone> getZoneById(String id) {
        return zoneRepository.findById(id);
    }

    public Zone createZone(ZoneDto zoneDto) {
        Zone zone = Zone.builder()
                .x(zoneDto.getX())
                .y(zoneDto.getY())
                .width(zoneDto.getWidth())
                .height(zoneDto.getHeight())
                .name(zoneDto.getName())
                .description(zoneDto.getDescription())
                .color(zoneDto.getColor())
                .build();
        return zoneRepository.save(zone);
    }

    public List<Zone> createZones(List<ZoneDto> zoneDtos) {
        List<Zone> zones = zoneDtos.stream()
                .map(zoneDto -> Zone.builder()
                        .x(zoneDto.getX())
                        .y(zoneDto.getY())
                        .width(zoneDto.getWidth())
                        .height(zoneDto.getHeight())
                        .name(zoneDto.getName())
                        .description(zoneDto.getDescription())
                        .color(zoneDto.getColor())
                        .build())
                .collect(Collectors.toList());
        return zoneRepository.saveAll(zones);
    }

    public Optional<Zone> updateZone(String id, ZoneDto zoneDto) {
        return zoneRepository.findById(id)
                .map(existingZone -> {
                    existingZone.setX(zoneDto.getX());
                    existingZone.setY(zoneDto.getY());
                    existingZone.setWidth(zoneDto.getWidth());
                    existingZone.setHeight(zoneDto.getHeight());
                    existingZone.setName(zoneDto.getName());
                    existingZone.setDescription(zoneDto.getDescription());
                    existingZone.setColor(zoneDto.getColor());
                    return zoneRepository.save(existingZone);
                });
    }

    public boolean deleteZone(String id) {
        if (zoneRepository.existsById(id)) {
            zoneRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // 인스턴싱을 위한 색상별 Zone 그룹화
    public Map<String, List<Zone>> getZonesByColorGroup() {
        List<Zone> allZones = zoneRepository.findAll();
        return allZones.stream()
                .collect(Collectors.groupingBy(zone -> 
                    zone.getColor() != null ? zone.getColor().toLowerCase() : "default"
                ));
    }

    // 특정 색상의 Zone들 조회
    public List<Zone> getZonesByColor(String color) {
        return zoneRepository.findByColor(color);
    }

    // 색상별 Zone 개수 조회
    public Map<String, Long> getZoneCountByColor() {
        List<Zone> allZones = zoneRepository.findAll();
        return allZones.stream()
                .collect(Collectors.groupingBy(
                    zone -> zone.getColor() != null ? zone.getColor().toLowerCase() : "default",
                    Collectors.counting()
                ));
    }
}
