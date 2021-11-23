package com.freenow.service.driver;

import com.freenow.controller.util.QueryParams;
import com.freenow.dataaccessobject.DriverRepository;
import com.freenow.dataaccessobject.specification.DriverSpecification;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.GeoCoordinate;
import com.freenow.domainvalue.OnlineStatus;
import com.freenow.exception.CarAlreadyInUseException;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.DriverOfflineException;
import com.freenow.exception.EntityNotFoundException;

import java.util.List;

import com.freenow.service.car.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to encapsulate the link between DAO and controller and to have business logic for some driver specific things.
 * <p/>
 */
@Service
public class DefaultDriverService implements DriverService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultDriverService.class);

    private final DriverRepository driverRepository;
    private final CarService carService;

    @Autowired
    public DefaultDriverService(final DriverRepository driverRepository, final CarService carService) {
        this.driverRepository = driverRepository;
        this.carService = carService;
    }


    /**
     * Selects a driver by id.
     *
     * @param driverId
     * @return found driver
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    public DriverDO find(Long driverId) throws EntityNotFoundException {
        return findDriverChecked(driverId);
    }


    /**
     * Creates a new driver.
     *
     * @param driverDO
     * @return newly created driver
     * @throws ConstraintsViolationException if a driver already exists with the given username, ... .
     */
    @Override
    public DriverDO create(DriverDO driverDO) throws ConstraintsViolationException {
        DriverDO driver;
        try {
            driver = driverRepository.save(driverDO);
        } catch (DataIntegrityViolationException e) {
            LOG.warn("ConstraintsViolationException while creating a driver: {}", driverDO, e);
            throw new ConstraintsViolationException(e.getMessage());
        }
        return driver;
    }


    /**
     * Deletes an existing driver by id.
     *
     * @param driverId
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    @Transactional
    public void delete(Long driverId) throws EntityNotFoundException {
        DriverDO driverDO = findDriverChecked(driverId);
        driverDO.setDeleted(true);
        driverDO.setCar(null);
    }


    /**
     * Updates the location for a driver.
     *
     * @param driverId
     * @param longitude
     * @param latitude
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    @Transactional
    public void updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException {
        DriverDO driverDO = findDriverChecked(driverId);
        driverDO.setCoordinate(new GeoCoordinate(latitude, longitude));
    }


    /**
     * Selects a car for an online driver.
     *
     * @param driverId
     * @param carId
     * @throws EntityNotFoundException  if no driver with the given id was found.
     * @throws DriverOfflineException   if driver is offline.
     * @throws CarAlreadyInUseException if a driver tries to select already in use car.
     */
    @Override
    @Transactional
    public void selectCar(long driverId, long carId) throws EntityNotFoundException, DriverOfflineException, CarAlreadyInUseException {
        DriverDO driverDO = findDriverChecked(driverId);

        if (driverDO.getOnlineStatus() == OnlineStatus.OFFLINE) throw new DriverOfflineException("Driver is offline.");

        CarDO carDO = carService.find(carId);
        DriverDO carDriverDO = driverRepository.findByOnlineStatusAndCar_Id(OnlineStatus.ONLINE, carId);

        if (carDriverDO != null) {
            if (carDriverDO.getId() == driverDO.getId()) return;
            else throw new CarAlreadyInUseException("Car already in use.");
        }

        driverDO.setCar(carDO);
    }


    /**
     * Deselects the car for an online driver.
     *
     * @param driverId
     * @throws EntityNotFoundException if no driver with the given id was found.
     */
    @Override
    @Transactional
    public void deselectCar(long driverId) throws EntityNotFoundException {
        DriverDO driverDO = findDriverChecked(driverId);
        driverDO.setCar(null);
    }


    /**
     * Finds all drivers by query params (username, online status, rating, manufacturer, and license plate).
     *
     * @return found driver list
     * @param queryParams
     */
    public List<DriverDO> find(QueryParams queryParams) {
        return driverRepository.findAll(Specification.where(DriverSpecification.findByQueryParams(queryParams)));
    }

    private DriverDO findDriverChecked(Long driverId) throws EntityNotFoundException {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find driver with id: " + driverId));
    }
}
