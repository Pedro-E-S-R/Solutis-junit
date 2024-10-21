package com.mightyjava.service;

import java.util.List;
import java.util.Optional;

import com.mightyjava.model.Address;

public interface AddressService {
	List<Address> addressList();
	
	Optional<Address> findOne(Long id);
	
	String addAddress(Address address);
	
	String deleteAddress(Long id);
}
