package com.freenow.dataaccessobject;

import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.OnlineStatus;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Database Access Object for driver table.
 * <p/>
 */
public interface DriverRepository extends CrudRepository<DriverDO, Long>, JpaSpecificationExecutor<DriverDO>
{
    DriverDO findByOnlineStatusAndCar_Id(OnlineStatus onlineStatus, Long carId);
}
