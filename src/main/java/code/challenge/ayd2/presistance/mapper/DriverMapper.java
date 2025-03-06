package code.challenge.ayd2.presistance.mapper;

import code.challenge.ayd2.domain.dto.DriverCreateDto;
import code.challenge.ayd2.presentation.entity.DriverEntity;
import org.springframework.stereotype.Component;

@Component
public class DriverMapper {

    public DriverCreateDto toDriverCreateDto(DriverEntity driver) {
        return new DriverCreateDto(driver.getName(), driver.getAge());
    }

    public DriverEntity toDriverEntity(DriverCreateDto dto) {
        return new DriverEntity(dto.name(), dto.age());
    }
}
