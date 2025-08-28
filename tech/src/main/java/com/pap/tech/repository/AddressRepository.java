package com.pap.tech.repository;

import com.pap.tech.entity.Address;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, String> {
    List<Address> findByUser_Id(String id, Sort sort);
    @Modifying
    @Query("UPDATE Address a SET a.isdefault = false WHERE a.user.id = :userId")
    void updateIsDefaultFalseByUser(@Param("userId")String userId);
    @Query("SELECT a FROM Address a WHERE a.user.id = :userId AND a.isdefault = true")
    Address findDefaultByUserId(@Param("userId") String userId);
}
