package com.example.pizzeria.Services;

import com.example.pizzeria.DTOs.ClientDTO;
import com.example.pizzeria.Entities.Client;
import com.example.pizzeria.Entities.Image;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClientService {
    ClientDTO addClient(Client client);
    Page<ClientDTO> getAllClients(Integer pageNumber, Integer pageSize);
    int getClientsNumber();
    ClientDTO getClientById(Long clientId);
    ClientDTO updateClient(ClientDTO clientDTO);
    ClientDTO updateClientImage(Long clientId, Image image);
    void deleteClient(Long clientId);
    boolean passwordVerification(String password, Long clientId);
    ClientDTO passwordUpdate(String password, Long clientId);
    List<ClientDTO> findTop10ByOrderByIdDesc();

}
