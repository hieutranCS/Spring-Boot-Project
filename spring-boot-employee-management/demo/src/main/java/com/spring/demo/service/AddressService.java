package com.spring.demo.service;

import java.util.List;

import com.spring.demo.entity.Address;
import com.spring.demo.entity.Employee;

public interface AddressService {
    List<Address> getAllAddress();
    void saveAddress(Address address,Employee employee);
    Address getAddressByEmployeeId(Long id);
    void deleteAddressByEmployeeId(long id);

}
