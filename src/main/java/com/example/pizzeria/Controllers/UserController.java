package com.example.pizzeria.Controllers;

import com.example.pizzeria.Configuration.AuthenticationRequest;
import com.example.pizzeria.Configuration.AuthenticationResponse;
import com.example.pizzeria.Entities.User;
import com.example.pizzeria.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @PostMapping("/authenticate")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request) {
        return userService.login(request);
    }

    @GetMapping("/email/{email}")
    public Optional<User> findByEmail(@PathVariable("email") String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/{id}")
    public User findByEmail(@PathVariable("id") Long id) {
        return userService.getUserByID(id);
    }
}
