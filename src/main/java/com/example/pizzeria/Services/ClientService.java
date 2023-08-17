package com.example.pizzeria.Services;

import com.example.pizzeria.DTOs.ClientDTO;
import com.example.pizzeria.Entities.Client;

import java.util.List;

public interface ClientService {
    ClientDTO addClient(Client client);
    List<ClientDTO> getAllClients();
    ClientDTO getClientById(Long clientId);
    ClientDTO updateClient(ClientDTO clientDTO);
    void deleteClient(Long clientId);
}
