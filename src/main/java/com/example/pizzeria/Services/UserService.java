package com.example.pizzeria.Services;

import com.example.pizzeria.Configuration.AuthenticationRequest;
import com.example.pizzeria.Configuration.AuthenticationResponse;
import com.example.pizzeria.Entities.User;

import java.util.Optional;

public interface UserService {

    AuthenticationResponse login(AuthenticationRequest request);
    Optional<User> getUserByEmail(String email);
    User getUserByID(Long id);
}
