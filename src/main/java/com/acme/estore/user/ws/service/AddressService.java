package com.acme.estore.user.ws.service;

import java.util.List;

import com.acme.estore.user.ws.dto.AddressDTO;

public interface AddressService {
	List<AddressDTO> getAddresses(String usersId);
	AddressDTO getAddress(String addressId);
}
