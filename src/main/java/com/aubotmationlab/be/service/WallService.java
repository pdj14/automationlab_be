package com.automationlab.be.service;

import com.automationlab.be.dto.WallDto;
import com.automationlab.be.model.Wall;
import com.automationlab.be.repository.WallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WallService {

    private final WallRepository wallRepository;

    public List<Wall> getAllWalls() {
        return wallRepository.findAll();
    }

    public Optional<Wall> getWallById(String id) {
        return wallRepository.findById(id);
    }

    public Wall createWall(WallDto wallDto) {
        Wall wall = Wall.builder()
                .startX(wallDto.getStartX())
                .startY(wallDto.getStartY())
                .endX(wallDto.getEndX())
                .endY(wallDto.getEndY())
                .isGlass(wallDto.getIsGlass())
                .build();
        return wallRepository.save(wall);
    }

    public List<Wall> createWalls(List<WallDto> wallDtos) {
        List<Wall> walls = wallDtos.stream()
                .map(wallDto -> Wall.builder()
                        .startX(wallDto.getStartX())
                        .startY(wallDto.getStartY())
                        .endX(wallDto.getEndX())
                        .endY(wallDto.getEndY())
                        .isGlass(wallDto.getIsGlass())
                        .build())
                .collect(Collectors.toList());
        return wallRepository.saveAll(walls);
    }

    public Optional<Wall> updateWall(String id, WallDto wallDto) {
        return wallRepository.findById(id)
                .map(existingWall -> {
                    existingWall.setStartX(wallDto.getStartX());
                    existingWall.setStartY(wallDto.getStartY());
                    existingWall.setEndX(wallDto.getEndX());
                    existingWall.setEndY(wallDto.getEndY());
                    existingWall.setIsGlass(wallDto.getIsGlass());
                    return wallRepository.save(existingWall);
                });
    }

    public boolean deleteWall(String id) {
        if (wallRepository.existsById(id)) {
            wallRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
