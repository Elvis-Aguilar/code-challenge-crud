package code.challenge.ayd2.presentation.repository;

import code.challenge.ayd2.presentation.entity.DriverEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DriverRepository extends CrudRepository<DriverEntity, Long> {

    Optional<DriverEntity> findByName(String name);
}
