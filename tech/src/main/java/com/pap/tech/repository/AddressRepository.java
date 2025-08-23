package com.pap.tech.repository;

import com.pap.tech.entity.Address;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, String> {
    List<Address> findByUser_Id(String id, Sort sort);
}
