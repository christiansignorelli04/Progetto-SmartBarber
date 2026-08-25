package com.smartbarber.smartbarber.controller;

import com.smartbarber.smartbarber.entity.User;
import com.smartbarber.smartbarber.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize; // <-- IMPORT AGGIUNTO
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // l'admin vede tutti i clienti e blocchiamo l'accesso ai semplici utenti
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // info del singolo cliente, può essere visto sia dal cliente che dall'admin
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // salviamo un nuovo utente, inserimento manuale fatto dall'admin
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
}