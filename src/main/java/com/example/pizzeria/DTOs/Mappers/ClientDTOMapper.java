package com.example.pizzeria.DTOs.Mappers;

import com.example.pizzeria.DTOs.ClientDTO;
import com.example.pizzeria.Entities.Client;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ClientDTOMapper implements Function<Client, ClientDTO> {

    @Override
    public ClientDTO apply(Client client) {
        return new ClientDTO(
                client.getId(),
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(),
                client.getRole(),
                client.getPhoneNumber(),
                client.getImage()
        );
    }
}
