package code.challenge.ayd2.domain.service;

import code.challenge.ayd2.application.exception.BadRequestException;
import code.challenge.ayd2.application.exception.RequestConflictException;
import code.challenge.ayd2.domain.dto.DriverCreateDto;
import code.challenge.ayd2.presentation.entity.DriverEntity;
import code.challenge.ayd2.presentation.repository.DriverRepository;
import code.challenge.ayd2.presistance.mapper.DriverMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverMapper driverMapper;

    @InjectMocks
    private DriverService driverService;

    private DriverCreateDto driverDto;
    private DriverEntity driverEntity;

    @BeforeEach
    void setUp() {
        driverDto = new DriverCreateDto("Elvis Aguilar", 15);
        driverEntity = new DriverEntity();
        driverEntity.setName("Elvis Aguilar");
    }

    @Test
    void createDriver_WhenDriverDoesNotExist_ShouldCreateDriver() {
        // Arrange
        when(driverRepository.findByName(driverDto.name())).thenReturn(Optional.empty());
        when(driverMapper.toDriverEntity(driverDto)).thenReturn(driverEntity);
        when(driverMapper.toDriverCreateDto(driverEntity)).thenReturn(driverDto);

        // Act
        Optional<DriverCreateDto> result = driverService.CreateDriver(driverDto);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(driverDto.name(), result.get().name());
        verify(driverRepository, times(1)).findByName(driverDto.name());
        verify(driverMapper, times(1)).toDriverEntity(driverDto);
        verify(driverMapper, times(1)).toDriverCreateDto(driverEntity);
    }

    @Test
    void createDriver_WhenDriverExists_ShouldThrowException() {
        // Arrange
        when(driverRepository.findByName(driverDto.name())).thenReturn(Optional.of(driverEntity));

        // Act & Assert
        assertThrows(RequestConflictException.class, () -> driverService.CreateDriver(driverDto));
        verify(driverRepository, times(1)).findByName(driverDto.name());
        verify(driverMapper, never()).toDriverEntity(any());
    }

    @Test
    void getDriverById_WhenDriverExists_ShouldReturnDriver() {
        // Arrange
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driverEntity));
        when(driverMapper.toDriverCreateDto(driverEntity)).thenReturn(driverDto);

        // Act
        Optional<DriverCreateDto> result = driverService.getDriverById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(driverDto.name(), result.get().name());
        verify(driverRepository, times(1)).findById(1L);
        verify(driverMapper, times(1)).toDriverCreateDto(driverEntity);
    }

    @Test
    void getDriverById_WhenDriverDoesNotExist_ShouldThrowException() {
        // Arrange
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class, () -> driverService.getDriverById(1L));
        verify(driverRepository, times(1)).findById(1L);
    }

    @Test
    void getAllDrivers_ShouldReturnListOfDrivers() {
        // Arrange
        List<DriverEntity> entities = List.of(driverEntity);
        when(driverRepository.findAll()).thenReturn(entities);
        when(driverMapper.toDriverCreateDto(driverEntity)).thenReturn(driverDto);

        // Act
        List<DriverCreateDto> result = driverService.getAllDrivers();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(driverDto.name(), result.get(0).name());
        verify(driverRepository, times(1)).findAll();
        verify(driverMapper, times(1)).toDriverCreateDto(driverEntity);
    }
}
