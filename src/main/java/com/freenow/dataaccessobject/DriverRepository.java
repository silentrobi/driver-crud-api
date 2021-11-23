package com.freenow.dataaccessobject;

import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.OnlineStatus;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Database Access Object for driver table.
 * <p/>
 */
public interface DriverRepository extends CrudRepository<DriverDO, Long>, JpaSpecificationExecutor<DriverDO> {

    @Query("select e from DriverDO e left join e.car f where e.deleted=false and e.onlineStatus =?1 and f.id=?2")
    DriverDO findByOnlineStatusAndCar_Id(OnlineStatus onlineStatus, Long carId);

    @Override
    @Query("select e from DriverDO e where e.deleted=false")
    Iterable<DriverDO> findAll();

    @Override
    @Query("select e from DriverDO e where e.deleted=false and e.id=?1")
    Optional<DriverDO> findById(Long carId);
}
