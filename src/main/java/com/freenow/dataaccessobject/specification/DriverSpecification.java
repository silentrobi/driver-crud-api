package com.freenow.dataaccessobject.specification;

import com.freenow.controller.mapper.QueryParams;
import com.freenow.datatransferobject.DriverDTO;
import com.freenow.domainobject.CarDO;
import com.freenow.domainobject.DriverDO;
import com.freenow.domainvalue.Manufacturer;
import com.freenow.domainvalue.OnlineStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DriverSpecification {


//    public static Specification<DriverDO> findDriverByFilter(QueryParams queryParams){
//
//        return ((root, criteriaQuery, criteriaBuilder) ->{
//            List<Predicate> predicateList = new ArrayList<>();
//            if(queryParams.getUsername() != null ){
//                predicateList.add( criteriaBuilder.equal(root.get("username"), queryParams.getUsername()));
//            }
//            if(queryParams.getOnlineStatus() != null){
//                predicateList.add(criteriaBuilder.equal(root.get("onlineStatus"), queryParams.getUsername()));
//            }
//            final Path<CarDO> brandPath = root.get("car");
//            if(queryParams.getLicensePlate() != null){
//                predicateList.add(criteriaBuilder.equal(brandPath.<String>get("licensePlate"), queryParams.getLicensePlate()));
//            }
//
//            return criteriaBuilder.and(predicateList.toArray());
//        });
//    }

    public static Specification<DriverDO> findByUsername(String username){
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("username"), username));
    }

    public static Specification<DriverDO> findByOnlineStatus(OnlineStatus onlineStatus){
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("onlineStatus"), onlineStatus));
    }

    public static Specification<DriverDO> findByLicensePlate(String licensePlate){
        return ((root, criteriaQuery, criteriaBuilder) -> {
            final Path<CarDO> brandPath = root.get("car");
            return criteriaBuilder.equal(brandPath.<String>get("licensePlate"), licensePlate);
        });
    }

    public static Specification<DriverDO> findByRating(String rating){
        return ((root, criteriaQuery, criteriaBuilder) -> {
            final Path<CarDO> brandPath = root.get("car");
            return criteriaBuilder.equal(brandPath.<String>get("rating"), rating);
        });
    }

    public static Specification<DriverDO> findByRating(Manufacturer manufacturer){
        return ((root, criteriaQuery, criteriaBuilder) -> {
            final Path<CarDO> brandPath = root.get("car");
            return criteriaBuilder.equal(brandPath.<String>get("manufacturer"), manufacturer);
        });
    }
}
