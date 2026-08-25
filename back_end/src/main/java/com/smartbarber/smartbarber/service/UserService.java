package com.smartbarber.smartbarber.service;

import com.smartbarber.smartbarber.entity.User;
import com.smartbarber.smartbarber.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // recupera tutti gli utenti (la usa solo l'admin)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // recupera un singolo utente tramite ID
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Utente non trovato nel database"));
    }

    // registra un nuovo utente nel sistema
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }
}