package code.challenge.ayd2.presistance.controller;

import code.challenge.ayd2.domain.dto.DriverCreateDto;
import code.challenge.ayd2.domain.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/drivers")
public class DriverController {

    private final DriverService driverService;

    @PostMapping
    public ResponseEntity<DriverCreateDto> createDriver(@RequestBody DriverCreateDto driver) {
        Optional<DriverCreateDto> createdDriver = driverService.CreateDriver(driver);
        return createdDriver.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Driver with name already exists"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverCreateDto> getDriverById(@PathVariable Long id) {
        Optional<DriverCreateDto> driver = driverService.getDriverById(id);
        return driver.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Driver with this ID does not exist"));
    }

    @GetMapping
    public ResponseEntity<List<DriverCreateDto>> getAllDrivers() {
        List<DriverCreateDto> drivers = driverService.getAllDrivers();
        return ResponseEntity.ok(drivers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverCreateDto> updateDriver(@PathVariable Long id, @RequestBody DriverCreateDto driver) {
        Optional<DriverCreateDto> updatedDriver = driverService.updateDriver(id, driver);
        return updatedDriver.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Driver with this ID does not exist"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        driverService.deleteDriverById(id);
        return ResponseEntity.noContent().build();
    }

}
