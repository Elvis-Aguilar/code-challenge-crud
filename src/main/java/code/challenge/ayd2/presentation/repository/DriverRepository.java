package code.challenge.ayd2.presentation.repository;

import code.challenge.ayd2.presentation.entity.DriverEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends CrudRepository<DriverEntity, Long> {

    Optional<DriverEntity> findByName(String name);
    boolean existsByName(String name);
}
