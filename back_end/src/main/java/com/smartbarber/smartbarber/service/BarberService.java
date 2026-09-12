package com.smartbarber.smartbarber.service;

import com.smartbarber.smartbarber.entity.Barber;
import com.smartbarber.smartbarber.entity.Salon;
import com.smartbarber.smartbarber.repository.BarberRepository;
import com.smartbarber.smartbarber.repository.SalonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BarberService {

    private final BarberRepository barberRepository;
    private final SalonRepository salonRepository; // lo uso per trovare il salone del gestore

    public List<Barber> getAvailableBarbersBySalon(Long salonId) {
        return barberRepository.findBySalonIdAndIsActiveTrue(salonId);
    }

    // aggiungo un barbiere al salone del gestore loggato
    @Transactional
    public Barber addBarberToMySalon(String name, String ownerKeycloakId) {

        // trovo il salone di proprietà di chi sta facendo la richiesta
        Salon mySalon = salonRepository.findByOwnerKeycloakId(ownerKeycloakId)
                .orElseThrow(() -> new RuntimeException("Devi prima creare il tuo salone nell'Area Business prima di poter aggiungere barbieri!"));

        // creao il barbiere e lo leghiamo indissolubilmente a questo salone
        Barber barber = new Barber();
        barber.setName(name);
        barber.setSalon(mySalon);

        return barberRepository.save(barber);
    }

    @Transactional
    public void softDeleteBarber(Long id) {
        Barber barber = barberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barbiere non trovato"));

        barber.setActive(false); // faccio un Soft Delete
        barberRepository.save(barber);
    }

    public List<Barber> getMyBarbers(String ownerKeycloakId) {
        Salon mySalon = salonRepository.findByOwnerKeycloakId(ownerKeycloakId).orElse(null);
        if (mySalon == null) {
            return List.of();
        }
        // uso la query creata prima per prendere solo quelli non licenziati
        return barberRepository.findBySalonIdAndIsActiveTrue(mySalon.getId());
    }
}