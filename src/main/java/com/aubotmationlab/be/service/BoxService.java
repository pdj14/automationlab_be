package com.aubotmationlab.be.service;

import com.aubotmationlab.be.dto.BoxDto;
import com.aubotmationlab.be.model.Box;
import com.aubotmationlab.be.repository.BoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                .width(boxDto.getWidth())
                .height(boxDto.getHeight())
                .depth(boxDto.getDepth())
                .color(boxDto.getColor())
                .build();
        return boxRepository.save(box);
    }

    public List<Box> createBoxes(List<BoxDto> boxDtos) {
        List<Box> boxes = boxDtos.stream()
                .map(boxDto -> Box.builder()
                        .x(boxDto.getX())
                        .y(boxDto.getY())
                        .width(boxDto.getWidth())
                        .height(boxDto.getHeight())
                        .depth(boxDto.getDepth())
                        .color(boxDto.getColor())
                        .build())
                .collect(Collectors.toList());
        return boxRepository.saveAll(boxes);
    }

    public Optional<Box> updateBox(String id, BoxDto boxDto) {
        return boxRepository.findById(id)
                .map(existingBox -> {
                    existingBox.setX(boxDto.getX());
                    existingBox.setY(boxDto.getY());
                    existingBox.setWidth(boxDto.getWidth());
                    existingBox.setHeight(boxDto.getHeight());
                    existingBox.setDepth(boxDto.getDepth());
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

