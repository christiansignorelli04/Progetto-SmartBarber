package com.smartbarber.smartbarber.service;
import java.util.Optional;
import com.smartbarber.smartbarber.entity.Salon;
import com.smartbarber.smartbarber.entity.User;
import com.smartbarber.smartbarber.repository.SalonRepository;
import com.smartbarber.smartbarber.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalonService {

    private final SalonRepository salonRepository;
    private final UserRepository userRepository;

    public List<Salon> getAllSalons() {
        return salonRepository.findAll();
    }

    public List<Salon> getSalonsByCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            return getAllSalons();
        }
        return salonRepository.findByCityContainingIgnoreCase(city.trim());
    }

    @Transactional
    public Salon addSalon(String name, String city, String address, String closedDays, String ownerKeycloakId) {
        User owner = userRepository.findAll().stream()
                .filter(u -> u.getKeycloakId().equals(ownerKeycloakId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Utente gestore non trovato"));

        if (salonRepository.findByOwnerKeycloakId(ownerKeycloakId).isPresent()) {
            throw new RuntimeException("Hai già creato il tuo salone!");
        }

        Salon salon = new Salon();
        salon.setName(name);
        salon.setCity(city.toLowerCase());
        salon.setAddress(address);
        salon.setClosedDays(closedDays);
        salon.setOwner(owner);
        return salonRepository.save(salon);
    }
}