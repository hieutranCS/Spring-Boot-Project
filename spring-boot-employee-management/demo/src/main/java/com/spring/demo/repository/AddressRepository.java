package com.spring.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.demo.entity.Address;

@Transactional
@Repository
public interface AddressRepository  extends JpaRepository<Address, Long>{
    Optional<Address> getAddressByEmployeeEmployeeId(Long id);

    void deleteByEmployeeEmployeeId(long id);


}
