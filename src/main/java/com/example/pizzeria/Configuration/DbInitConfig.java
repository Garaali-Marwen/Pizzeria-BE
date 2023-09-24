package com.example.pizzeria.Configuration;

import com.example.pizzeria.Entities.Admin;
import com.example.pizzeria.Entities.Category;
import com.example.pizzeria.Entities.Config;
import com.example.pizzeria.Enum.Role;
import com.example.pizzeria.Repositories.AdminRepository;
import com.example.pizzeria.Repositories.CategoryRepository;
import com.example.pizzeria.Repositories.ConfigRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component
@AllArgsConstructor
public class DbInitConfig implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final ConfigRepository configRepository;

    @Override
    public void run(String... args) throws Exception {
        if (adminRepository.count() == 0) {
            Admin admin = new Admin();
            admin.setPassword(passwordEncoder.encode("mmmmmmmm"));
            admin.setRole(Role.ADMIN);
            admin.setEmail("admin@admin.com");
            admin.setFirstName("Super");
            admin.setLastName("Admin");
            adminRepository.save(admin);
        }
        if (categoryRepository.count() == 0) {
            Category drinks = new Category();
            drinks.setName("Boissons");

            Category pizzas = new Category();
            pizzas.setName("pizzas");

            Category sandwichs = new Category();
            sandwichs.setName("sandwichs");
            categoryRepository.saveAll(Arrays.asList(drinks, pizzas, sandwichs));
        }
        if (configRepository.count() == 0) {
            Config config = new Config();
            config.setDeliveryPhone("1-320-544-8749");
            config.setContactPhone("1-320-544-8749");
            config.setContactEmail("garaali.marwen@gmail.com");
            configRepository.save(config);
        }
    }
}