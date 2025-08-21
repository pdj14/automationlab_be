package com.aubotmationlab.be.service;

import com.aubotmationlab.be.dto.WallDto;
import com.aubotmationlab.be.model.Wall;
import com.aubotmationlab.be.repository.WallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
