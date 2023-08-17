package com.example.pizzeria.Controllers;

import com.example.pizzeria.DTOs.ClientDTO;
import com.example.pizzeria.Entities.Client;
import com.example.pizzeria.Services.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/add")
    public ClientDTO addClient(@RequestBody Client client){
        return clientService.addClient(client);
    }

    @GetMapping("/all")
    public List<ClientDTO> getAllClients(){
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public ClientDTO getClientById(@PathVariable("id") Long clientId){
        return clientService.getClientById(clientId);
    }

    @PutMapping("/update")
    public ClientDTO updateClient(@RequestBody ClientDTO clientDTO){
        return clientService.updateClient(clientDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteClient(@PathVariable("id") Long clientId){
        clientService.deleteClient(clientId);
    }

}
