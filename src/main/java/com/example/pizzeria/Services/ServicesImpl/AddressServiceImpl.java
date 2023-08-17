package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.Entities.Address;
import com.example.pizzeria.Repositories.AddressRepository;
import com.example.pizzeria.Services.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    @Override
    public Address addAddress(Address address) {
        return addressRepository.save(address);
    }
}
