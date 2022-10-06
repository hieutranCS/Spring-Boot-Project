package com.spring.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.demo.entity.Address;
import com.spring.demo.entity.Employee;
import com.spring.demo.repository.AddressRepository;

@Service
public class AddressServiceImpl implements AddressService {    

    @Autowired
	private AddressRepository addressRepository;

	@Override
	public List<Address> getAllAddress() {
		return addressRepository.findAll();
	}

	@Override
	public Address getAddressByEmployeeId(Long id) {
		Optional<Address> optional = addressRepository.getAddressByEmployeeEmployeeId(id);
		Address address = new Address();
		if (optional.isPresent()) {
			return address = optional.get();
		} 
		else {
			return address;
		}
	}

	@Override
	public void saveAddress(Address address,Employee employee) {
		address.setEmployee(employee);
		this.addressRepository.save(address);
	}

	@Override
	public void deleteAddressByEmployeeId(long id) {
		this.addressRepository.deleteByEmployeeEmployeeId(id);
		
	}

    
}
