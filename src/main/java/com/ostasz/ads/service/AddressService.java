package com.ostasz.ads.service;

import com.ostasz.ads.datamodel.dto.AddressDTO;
import com.ostasz.ads.datamodel.entity.Address;
import com.ostasz.ads.repository.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address addAddress(AddressDTO addressDTO) {
        if (addressDTO != null) {
            Address address = createAddress(addressDTO);
            addressRepository.save(address);
            return address;
        }
        return null;
    }

    private Address createAddress(AddressDTO addressDTO) {
        return Address.builder()
                .country(addressDTO.getCountry())
                .streetName(addressDTO.getStreetName())
                .houseNumber(addressDTO.getHouseNumber())
                .postalCode(addressDTO.getPostalCode())
                .build();
    }
}
