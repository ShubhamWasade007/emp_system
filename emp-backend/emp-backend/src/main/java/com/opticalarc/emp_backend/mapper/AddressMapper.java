package com.opticalarc.emp_backend.mapper;

import com.opticalarc.emp_backend.dto.AddressDto;
import com.opticalarc.emp_backend.entity.Address;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
//@NoArgsConstructor
public class AddressMapper {

    public AddressDto addressToAddressDto(Address address){
        return  AddressDto.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode()). build();
    }


    public Address addressDtoToAddress(AddressDto addressDto){
        return Address.builder()
                .id(addressDto.getId())
                .street(addressDto.getStreet())
                .city(addressDto.getCity())
                .state(addressDto.getState())
                .zipCode(addressDto.getZipCode()). build();
    }

}
