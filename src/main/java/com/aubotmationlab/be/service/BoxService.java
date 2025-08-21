package com.aubotmationlab.be.service;

import com.aubotmationlab.be.dto.BoxDto;
import com.aubotmationlab.be.model.Box;
import com.aubotmationlab.be.repository.BoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoxService {

    private final BoxRepository boxRepository;

    public List<Box> getAllBoxes() {
        return boxRepository.findAll();
    }

    public Optional<Box> getBoxById(String id) {
        return boxRepository.findById(id);
    }

    public Box createBox(BoxDto boxDto) {
        Box box = Box.builder()
                .x(boxDto.getX())
                .y(boxDto.getY())
                .widthScale(boxDto.getWidthScale())
                .heightScale(boxDto.getHeightScale())
                .depthScale(boxDto.getDepthScale())
                .color(boxDto.getColor())
                .build();
        return boxRepository.save(box);
    }

    public Optional<Box> updateBox(String id, BoxDto boxDto) {
        return boxRepository.findById(id)
                .map(existingBox -> {
                    existingBox.setX(boxDto.getX());
                    existingBox.setY(boxDto.getY());
                    existingBox.setWidthScale(boxDto.getWidthScale());
                    existingBox.setHeightScale(boxDto.getHeightScale());
                    existingBox.setDepthScale(boxDto.getDepthScale());
                    existingBox.setColor(boxDto.getColor());
                    return boxRepository.save(existingBox);
                });
    }

    public boolean deleteBox(String id) {
        if (boxRepository.existsById(id)) {
            boxRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

