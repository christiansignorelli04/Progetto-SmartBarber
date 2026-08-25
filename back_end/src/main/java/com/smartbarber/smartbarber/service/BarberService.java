package com.smartbarber.smartbarber.service;

import com.smartbarber.smartbarber.entity.Barber;
import com.smartbarber.smartbarber.repository.BarberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BarberService {

    private final BarberRepository barberRepository;

    // recupera solo i barbieri attivi
    public List<Barber> getAvailableBarbers() {
        return barberRepository.findAll().stream().filter(Barber::isAvailable).collect(Collectors.toList());
    }

    // aggiunge un nuovo barbiere
    @Transactional
    public Barber addBarber(String name) {
        Barber barber = new Barber();
        barber.setName(name);
        barber.setAvailable(true);
        return barberRepository.save(barber);
    }

    // non cancello il record ma metto il barbiere in ferie
    @Transactional
    public void softDeleteBarber(Long id) {
        Barber barber = barberRepository.findById(id).orElseThrow(() -> new RuntimeException("Barbiere non trovato"));
        barber.setAvailable(false);
        barberRepository.save(barber);
    }
}