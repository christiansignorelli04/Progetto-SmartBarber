package com.smartbarber.smartbarber.service;

import com.smartbarber.smartbarber.entity.User;
import com.smartbarber.smartbarber.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // recupero tutti gli utenti, lo usa solo l'admin
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // recupero un singolo utente tramite ID
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Utente non trovato nel database"));
    }

    // registro o aggiorno un utente nel sistema
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // faccio una ricerca tramite Keycloak ID
    public Optional<User> findByKeycloakId(String keycloakId) {
        return userRepository.findByKeycloakId(keycloakId);
    }
}