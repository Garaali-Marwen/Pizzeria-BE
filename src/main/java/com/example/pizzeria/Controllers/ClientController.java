package com.example.pizzeria.Controllers;

import com.example.pizzeria.Configuration.AuthenticationRequest;
import com.example.pizzeria.DTOs.ClientDTO;
import com.example.pizzeria.Entities.Client;
import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Services.ClientService;
import com.example.pizzeria.Services.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ClientController {

    private final ClientService clientService;
    private final ImageService imageService;


    @PostMapping("/add")
    public ClientDTO addClient(@RequestBody Client client) {
        return clientService.addClient(client);
    }

    @GetMapping("/all/{pageNumber}/{pageSize}")
    public Page<ClientDTO> getAllClients(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize) {
        return clientService.getAllClients(pageNumber, pageSize);
    }

    @GetMapping("/all")
    public int getClientsNumber() {
        return clientService.getClientsNumber();
    }

    @GetMapping("/{id}")
    public ClientDTO getClientById(@PathVariable("id") Long clientId) {
        return clientService.getClientById(clientId);
    }

    @PutMapping("/update")
    public ClientDTO updateClient(@RequestBody ClientDTO clientDTO) {
        return clientService.updateClient(clientDTO);
    }

    @PutMapping(value = "/update/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ClientDTO updateClient(@RequestPart("clientId") Long clientId,
                                  @RequestPart("image") MultipartFile image) {
        try {
            Image images = imageService.uploadFile(image);
            return clientService.updateClientImage(clientId, images);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteClient(@PathVariable("id") Long clientId) {
        clientService.deleteClient(clientId);
    }

    @PostMapping("/password/verification/{clientId}")
    public boolean passwordVerification(@RequestBody AuthenticationRequest authenticationRequest, @PathVariable("clientId") Long clientId) {
        return clientService.passwordVerification(authenticationRequest.getPassword(), clientId);
    }

    @PutMapping("/update/password/{clientId}")
    public ClientDTO updatePassword(@RequestBody AuthenticationRequest authenticationRequest, @PathVariable("clientId") Long clientId) {
        return clientService.passwordUpdate(authenticationRequest.getPassword(), clientId);
    }

    @GetMapping("/new")
    public List<ClientDTO> findTop10ByOrderByIdDesc() {
        return clientService.findTop10ByOrderByIdDesc();
    }

}
