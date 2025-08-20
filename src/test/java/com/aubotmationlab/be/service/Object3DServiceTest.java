package com.aubotmationlab.be.service;

import com.aubotmationlab.be.dto.Object3DDto;
import com.aubotmationlab.be.model.Object3D;
import com.aubotmationlab.be.model.Object3D.Category;
import com.aubotmationlab.be.repository.Object3DRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Object3DServiceTest {

    @Mock
    private Object3DRepository object3DRepository;

    @InjectMocks
    private Object3DService object3DService;

    private Object3DDto testObjectDto;
    private Object3D testObject;

    @BeforeEach
    void setUp() {
        testObjectDto = Object3DDto.builder()
                .name("Test Robot")
                .category(Category.ROBOT)
                .description("A test robot")
                .glbFile("/models/test-robot.glb")
                .width(1.0)
                .depth(1.0)
                .height(2.0)
                .rotation(0.0)
                .color("#FF0000")
                .instancingEnabled(true)
                .build();

        testObject = Object3D.builder()
                .id("test-id")
                .name("Test Robot")
                .category(Category.ROBOT)
                .description("A test robot")
                .glbFile("/models/test-robot.glb")
                .width(1.0)
                .depth(1.0)
                .height(2.0)
                .rotation(0.0)
                .color("#FF0000")
                .instancingEnabled(true)
                .build();
    }

    @Test
    void getAllObjects_ShouldReturnAllObjects() {
        // Given
        when(object3DRepository.findAll()).thenReturn(Arrays.asList(testObject));

        // When
        List<Object3DDto> result = object3DService.getAllObjects();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Robot", result.get(0).getName());
        verify(object3DRepository).findAll();
    }

    @Test
    void getObjectById_ShouldReturnObject_WhenExists() {
        // Given
        when(object3DRepository.findById("test-id")).thenReturn(Optional.of(testObject));

        // When
        Optional<Object3DDto> result = object3DService.getObjectById("test-id");

        // Then
        assertTrue(result.isPresent());
        assertEquals("Test Robot", result.get().getName());
        verify(object3DRepository).findById("test-id");
    }

    @Test
    void getObjectById_ShouldReturnEmpty_WhenNotExists() {
        // Given
        when(object3DRepository.findById("non-existent")).thenReturn(Optional.empty());

        // When
        Optional<Object3DDto> result = object3DService.getObjectById("non-existent");

        // Then
        assertFalse(result.isPresent());
        verify(object3DRepository).findById("non-existent");
    }

    @Test
    void createObject_ShouldCreateAndReturnObject_WhenValidInput() {
        // Given
        when(object3DRepository.existsByName("Test Robot")).thenReturn(false);
        when(object3DRepository.save(any(Object3D.class))).thenReturn(testObject);

        // When
        Object3DDto result = object3DService.createObject(testObjectDto);

        // Then
        assertNotNull(result);
        assertEquals("Test Robot", result.getName());
        verify(object3DRepository).existsByName("Test Robot");
        verify(object3DRepository).save(any(Object3D.class));
    }

    @Test
    void createObject_ShouldThrowException_WhenNameAlreadyExists() {
        // Given
        when(object3DRepository.existsByName("Test Robot")).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            object3DService.createObject(testObjectDto);
        });
        verify(object3DRepository).existsByName("Test Robot");
        verify(object3DRepository, never()).save(any(Object3D.class));
    }

    @Test
    void updateObject_ShouldUpdateAndReturnObject_WhenExists() {
        // Given
        Object3DDto updateDto = Object3DDto.builder()
                .name("Updated Robot")
                .category(Category.ROBOT)
                .glbFile("/models/updated-robot.glb")
                .width(2.0)
                .depth(2.0)
                .height(3.0)
                .rotation(90.0)
                .build();

        when(object3DRepository.findById("test-id")).thenReturn(Optional.of(testObject));
        when(object3DRepository.existsByName("Updated Robot")).thenReturn(false);
        when(object3DRepository.save(any(Object3D.class))).thenReturn(testObject);

        // When
        Optional<Object3DDto> result = object3DService.updateObject("test-id", updateDto);

        // Then
        assertTrue(result.isPresent());
        verify(object3DRepository).findById("test-id");
        verify(object3DRepository).save(any(Object3D.class));
    }

    @Test
    void deleteObject_ShouldReturnTrue_WhenExists() {
        // Given
        when(object3DRepository.existsById("test-id")).thenReturn(true);

        // When
        boolean result = object3DService.deleteObject("test-id");

        // Then
        assertTrue(result);
        verify(object3DRepository).existsById("test-id");
        verify(object3DRepository).deleteById("test-id");
    }

    @Test
    void deleteObject_ShouldReturnFalse_WhenNotExists() {
        // Given
        when(object3DRepository.existsById("non-existent")).thenReturn(false);

        // When
        boolean result = object3DService.deleteObject("non-existent");

        // Then
        assertFalse(result);
        verify(object3DRepository).existsById("non-existent");
        verify(object3DRepository, never()).deleteById(any());
    }
}
