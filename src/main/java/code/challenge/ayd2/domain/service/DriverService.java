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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    public Optional<DriverCreateDto> CreateDriver(DriverCreateDto driver) {
        // search driver with name
        boolean exist = this.driverRepository.existsByName(driver.name());

        if (exist) {
            throw new RequestConflictException("driver with name exist");
        }

        DriverEntity driverCreatedEntity = this.driverMapper.toDriverEntity(driver);
        driverCreatedEntity = this.driverRepository.save(driverCreatedEntity);

        return Optional.of(this.driverMapper.toDriverCreateDto(driverCreatedEntity));
    }

    public Optional<DriverCreateDto> getDriverById(Long id) {
        DriverEntity exist = this.driverRepository.findById(id).orElse(null);

        if (exist == null) {
            throw new BadRequestException("driver with id not exist");
        }

        return Optional.of(this.driverMapper.toDriverCreateDto(exist));

    }

    public List<DriverCreateDto> getAllDrivers() {
        Iterable<DriverEntity> iterable = this.driverRepository.findAll();

        return StreamSupport.stream(iterable.spliterator(), false)
                .map(driverMapper::toDriverCreateDto)
                .collect(Collectors.toList());
    }

    public Optional<DriverCreateDto> updateDriver(Long id, DriverCreateDto driver) {
        DriverEntity existingDriver = this.driverRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Driver with this ID does not exist"));

        existingDriver.setName(driver.name());
        existingDriver.setAge(driver.age());

        DriverEntity updatedDriver = this.driverRepository.save(existingDriver);

        return Optional.of(this.driverMapper.toDriverCreateDto(updatedDriver));
    }

    public void deleteDriverById(Long id) {

        if (!this.driverRepository.existsById(id)) {
            throw new BadRequestException("Driver with this ID does not exist");
        }

        this.driverRepository.deleteById(id);
    }

}
