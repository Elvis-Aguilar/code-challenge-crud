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
        driverEntity.setAge(15);
        driverEntity.setId(1L);
    }

    @Test
    void createDriver_WhenDriverDoesNotExist_ShouldCreateDriver() {
        // arrange
        when(driverRepository.existsByName(driverDto.name())).thenReturn(false);
        when(driverMapper.toDriverEntity(driverDto)).thenReturn(driverEntity);
        when(driverRepository.save(driverEntity)).thenReturn(driverEntity);
        when(driverMapper.toDriverCreateDto(driverEntity)).thenReturn(driverDto);

        //arc
        Optional<DriverCreateDto> result = driverService.CreateDriver(driverDto);

        // assert
        assertTrue(result.isPresent());
        assertEquals(driverDto.name(), result.get().name());
        verify(driverRepository, times(1)).existsByName(driverDto.name());
        verify(driverRepository, times(1)).save(driverEntity);
    }

    @Test
    void createDriver_WhenDriverExists_ShouldThrowException() {
        //arrange
        when(driverRepository.existsByName(driverDto.name())).thenReturn(true);

        // arc and assert
        assertThrows(RequestConflictException.class, () -> driverService.CreateDriver(driverDto));
        verify(driverRepository, times(1)).existsByName(driverDto.name());
        verify(driverRepository, never()).save(any());
    }

    @Test
    void getDriverById_WhenDriverExists_ShouldReturnDriver() {
        //arrange
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driverEntity));
        when(driverMapper.toDriverCreateDto(driverEntity)).thenReturn(driverDto);

        //arc
        Optional<DriverCreateDto> result = driverService.getDriverById(1L);

        // assert
        assertTrue(result.isPresent());
        assertEquals(driverDto.name(), result.get().name());
        verify(driverRepository, times(1)).findById(1L);
    }

    @Test
    void getDriverById_WhenDriverDoesNotExist_ShouldThrowException() {
        // arrange
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        // arc and assert
        assertThrows(BadRequestException.class, () -> driverService.getDriverById(1L));
        verify(driverRepository, times(1)).findById(1L);
    }

    @Test
    void getAllDrivers_ShouldReturnListOfDrivers() {
        // arrange
        List<DriverEntity> entities = List.of(driverEntity);
        when(driverRepository.findAll()).thenReturn(entities);
        when(driverMapper.toDriverCreateDto(driverEntity)).thenReturn(driverDto);

        // arc
        List<DriverCreateDto> result = driverService.getAllDrivers();

        // assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(driverDto.name(), result.get(0).name());
        verify(driverRepository, times(1)).findAll();
    }

    @Test
    void updateDriver_WhenDriverExists_ShouldUpdateDriver() {
        // arrange
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driverEntity));
        when(driverRepository.save(any(DriverEntity.class))).thenReturn(driverEntity);
        when(driverMapper.toDriverCreateDto(driverEntity)).thenReturn(driverDto);

        //arc
        Optional<DriverCreateDto> result = driverService.updateDriver(1L, driverDto);

        // assert
        assertTrue(result.isPresent());
        assertEquals(driverDto.name(), result.get().name());
        verify(driverRepository, times(1)).findById(1L);
        verify(driverRepository, times(1)).save(driverEntity);
    }

    @Test
    void updateDriver_WhenDriverDoesNotExist_ShouldThrowException() {
        // arrange
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        // arc and assert
        assertThrows(BadRequestException.class, () -> driverService.updateDriver(1L, driverDto));
        verify(driverRepository, times(1)).findById(1L);
    }

    @Test
    void deleteDriverById_WhenDriverExists_ShouldDeleteDriver() {
        // arrange
        when(driverRepository.existsById(1L)).thenReturn(true);

        // arc
        driverService.deleteDriverById(1L);

        // assert
        verify(driverRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteDriverById_WhenDriverDoesNotExist_ShouldThrowException() {
        when(driverRepository.existsById(1L)).thenReturn(false);

        assertThrows(BadRequestException.class, () -> driverService.deleteDriverById(1L));
        verify(driverRepository, times(1)).existsById(1L);
        verify(driverRepository, never()).deleteById(any());
    }
}
