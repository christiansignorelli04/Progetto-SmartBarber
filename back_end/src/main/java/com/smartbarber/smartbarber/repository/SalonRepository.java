package com.smartbarber.smartbarber.repository;
import java.util.Optional;
import com.smartbarber.smartbarber.entity.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalonRepository extends JpaRepository<Salon, Long> {

    // faccio una ricerca case-insensitive e parziale per trovare i saloni
    List<Salon> findByCityContainingIgnoreCase(String city);
    // trovo il salone posseduto da uno specifico utente tramite Keycloak ID
    Optional<Salon> findByOwnerKeycloakId(String keycloakId);
}