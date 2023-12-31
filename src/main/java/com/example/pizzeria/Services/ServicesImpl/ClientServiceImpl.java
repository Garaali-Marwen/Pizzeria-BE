package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.DTOs.ClientDTO;
import com.example.pizzeria.DTOs.Mappers.ClientDTOMapper;
import com.example.pizzeria.Entities.Client;
import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Enum.Role;
import com.example.pizzeria.Repositories.ClientRepository;
import com.example.pizzeria.Services.ClientService;
import com.example.pizzeria.Services.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final ImageService imageService;


    @Override
    public ClientDTO addClient(Client client) {
        client.setRole(Role.CLIENT);
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        return clientDTOMapper.apply(clientRepository.save(client));
    }

    @Override
    public Page<ClientDTO> getAllClients(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return clientRepository.findAll(pageable)
                .map(clientDTOMapper);
    }

    @Override
    public int getClientsNumber() {
        return clientRepository.findAll().size();
    }

    @Override
    public ClientDTO getClientById(Long clientId) {
        return clientDTOMapper.apply(clientRepository.findById(clientId)
                .orElseThrow(() -> new NoSuchElementException("No Client found with id: " + clientId)));
    }

    @Override
    public ClientDTO updateClient(ClientDTO clientDTO) {
        Client client = clientRepository.findById(clientDTO.id())
                .orElseThrow(() -> new NoSuchElementException("No client found with id: " + clientDTO.id()));
        client.setEmail(clientDTO.email());
        client.setFirstName(clientDTO.firstName());
        client.setLastName(clientDTO.lastName());
        client.setImage(clientDTO.image());
        client.setPhoneNumber(clientDTO.phoneNumber());
        clientRepository.save(client);
        return clientDTO;
    }

    @Override
    public ClientDTO updateClientImage(Long clientId, Image image) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NoSuchElementException("No client found with id: " + clientId));
        Image imageDelete = client.getImage();
        client.setImage(image);
        Client updatedClient = clientRepository.save(client);
        imageService.deleteImage(imageDelete.getId());
        return clientDTOMapper.apply(updatedClient);
    }

    @Override
    public void deleteClient(Long clientId) {
        clientRepository.deleteById(clientId);
    }

    @Override
    public boolean passwordVerification(String password, Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new NoSuchElementException("not found"));
        return passwordEncoder.matches(password, client.getPassword());
    }

    @Override
    public ClientDTO passwordUpdate(String password, Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new NoSuchElementException("Not found"));
        client.setPassword(passwordEncoder.encode(password));
        return clientDTOMapper.apply(clientRepository.save(client));
    }

    @Override
    public List<ClientDTO> findTop10ByOrderByIdDesc() {
        return clientRepository.findTop10ByOrderByIdDesc().stream().map(clientDTOMapper).collect(Collectors.toList());
    }
}
