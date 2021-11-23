package com.freenow.controller;

import com.freenow.controller.mapper.DriverMapper;
import com.freenow.controller.util.QueryParams;
import com.freenow.dataaccessobject.DriverRepository;
import com.freenow.datatransferobject.DriverDTO;
import com.freenow.domainobject.DriverDO;
import com.freenow.exception.CarAlreadyInUseException;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.DriverOfflineException;
import com.freenow.exception.EntityNotFoundException;
import com.freenow.service.driver.DriverService;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * All operations with a driver will be routed by this controller.
 * <p/>
 */
@RestController
@RequestMapping("v1/drivers")
public class DriverController {

    private final DriverService driverService;

    private final DriverRepository driverRepository;

    @Autowired
    public DriverController(final DriverService driverService, DriverRepository driverRepository) {
        this.driverService = driverService;
        this.driverRepository = driverRepository;
    }

    @GetMapping("/{driverId}")
    public DriverDTO getDriver(@PathVariable long driverId) throws EntityNotFoundException {
        return DriverMapper.makeDriverDTO(driverService.find(driverId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverDTO createDriver(@Valid @RequestBody DriverDTO driverDTO) throws ConstraintsViolationException {
        DriverDO driverDO = DriverMapper.makeDriverDO(driverDTO);
        return DriverMapper.makeDriverDTO(driverService.create(driverDO));
    }

    @DeleteMapping("/{driverId}")
    public void deleteDriver(@PathVariable long driverId) throws EntityNotFoundException {
        driverService.delete(driverId);
    }

    @PutMapping("/{driverId}")
    public void updateLocation(
            @PathVariable long driverId, @RequestParam double longitude, @RequestParam double latitude)
            throws EntityNotFoundException {
        driverService.updateLocation(driverId, longitude, latitude);
    }

    @PutMapping("/{driverId}/car/{carId}/select")
    public void selectCar(@PathVariable long driverId, @PathVariable long carId) throws EntityNotFoundException, DriverOfflineException, CarAlreadyInUseException {
        driverService.selectCar(driverId, carId);
    }

    @PutMapping("/{driverId}/car/deselect")
    public void deselectCar(@PathVariable long driverId) throws EntityNotFoundException {
        driverService.deselectCar(driverId);
    }

    @GetMapping
    public List<DriverDTO> findDrivers(QueryParams queryParams) {
        return DriverMapper.makeDriverDTOList(driverService.find(queryParams));
    }
}
