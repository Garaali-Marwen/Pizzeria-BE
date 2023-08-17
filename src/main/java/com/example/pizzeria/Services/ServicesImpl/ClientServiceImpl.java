package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.DTOs.ClientDTO;
import com.example.pizzeria.DTOs.Mappers.ClientDTOMapper;
import com.example.pizzeria.Entities.Client;
import com.example.pizzeria.Enum.Role;
import com.example.pizzeria.Repositories.ClientRepository;
import com.example.pizzeria.Services.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientDTOMapper clientDTOMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ClientDTO addClient(Client client) {
        client.setRole(Role.CLIENT);
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        return clientDTOMapper.apply(clientRepository.save(client));
    }

    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll()
                .stream().map(clientDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDTO getClientById(Long clientId) {
        return clientDTOMapper.apply(clientRepository.findById(clientId)
                .orElseThrow(() -> new NoSuchElementException("No Client found with id: " + clientId)));
    }

    @Override
    public ClientDTO updateClient(ClientDTO clientDTO) {
        Client client = clientRepository.findById(clientDTO.id())
                .orElseThrow(() -> new NoSuchElementException("No client found with id: "+ clientDTO.id()));
        client.setEmail(clientDTO.email());
        client.setFirstName(clientDTO.firstName());
        client.setLastName(clientDTO.lastName());
        client.setImage(clientDTO.image());
        client.setPhoneNumber(clientDTO.phoneNumber());
        clientRepository.save(client);
        return clientDTO;
    }

    @Override
    public void deleteClient(Long clientId) {
        clientRepository.deleteById(clientId);
    }
}
