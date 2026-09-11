package com.smartbarber.smartbarber.controller;

import com.smartbarber.smartbarber.entity.User;
import com.smartbarber.smartbarber.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // l'admin vede tutti i clienti e blocco l'accesso ai semplici utenti
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

    // salvo un nuovo utente, inserimento manuale fatto dall'admin
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // recupero il profilo dell'utente loggato e restituisco l'errore se non esiste
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<User> getMyProfile(org.springframework.security.core.Authentication authentication) {
        String keycloakId = authentication.getName();
        return userService.findByKeycloakId(keycloakId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // creo o aggiorno il profilo dell'utente loggato
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/me")
    public User saveMyProfile(@RequestBody User request, org.springframework.security.core.Authentication authentication) {
        String keycloakId = authentication.getName();

        // cerco l'user, se non lo trova crea un nuovo User
        User user = userService.findByKeycloakId(keycloakId).orElse(new User());

        user.setKeycloakId(keycloakId);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());

        // leggo i permessi reali dal Token di Keycloak
        boolean isOwner = authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("SHOP_OWNER"));
        if (isOwner) {
            user.setRole("SHOP_OWNER");
        } else {
            user.setRole("ROLE_USER"); // ruolo base di default
        }
        return userService.createUser(user);
    }
}