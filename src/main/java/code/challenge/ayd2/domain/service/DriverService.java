package code.challenge.ayd2.domain.service;

import code.challenge.ayd2.application.exception.BadRequestException;
import code.challenge.ayd2.application.exception.RequestConflictException;
import code.challenge.ayd2.domain.dto.DriverCreateDto;
import code.challenge.ayd2.presentation.entity.DriverEntity;
import code.challenge.ayd2.presentation.repository.DriverRepository;
import code.challenge.ayd2.presistance.mapper.DriverMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    public Optional<DriverCreateDto> CreateDriver(DriverCreateDto driver) {
        // search driver with name
        DriverEntity exist = this.driverRepository.findByName(driver.name()).orElse(null);

        if (exist != null) {
            throw new RequestConflictException("driver with name exist");
        }

        DriverEntity driverCreatedEntity = this.driverMapper.toDriverEntity(driver);

        return Optional.of(this.driverMapper.toDriverCreateDto(driverCreatedEntity));
    }

    public Optional<DriverCreateDto> getDriverById(Long id) {
        DriverEntity exist = this.driverRepository.findById(id).orElse(null);

        if (exist != null) {
            throw new BadRequestException("driver with id not exist");
        }

        return Optional.of(this.driverMapper.toDriverCreateDto(exist));

    }

    public List<DriverCreateDto> getAllDrivers() {
        return this.driverRepository.findAll()
    }


}
