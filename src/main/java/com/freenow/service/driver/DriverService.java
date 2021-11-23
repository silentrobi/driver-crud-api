package com.freenow.service.driver;

import com.freenow.controller.util.QueryParams;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.OnlineStatus;
import com.freenow.exception.CarAlreadyInUseException;
import com.freenow.exception.ConstraintsViolationException;
import com.freenow.exception.DriverOfflineException;
import com.freenow.exception.EntityNotFoundException;

import java.util.List;

public interface DriverService {
    
    DriverDO find(Long driverId) throws EntityNotFoundException;

    DriverDO create(DriverDO driverDO) throws ConstraintsViolationException;

    void delete(Long driverId) throws EntityNotFoundException;

    void updateLocation(long driverId, double longitude, double latitude) throws EntityNotFoundException;

    void selectCar(long driverId, long carId) throws EntityNotFoundException, DriverOfflineException, CarAlreadyInUseException;

    void deselectCar(long driverId) throws EntityNotFoundException;

    List<DriverDO> find(QueryParams queryParams);
}
