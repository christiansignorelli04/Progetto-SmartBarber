package com.smartbarber.smartbarber.controller;

import com.smartbarber.smartbarber.entity.Barber;
import com.smartbarber.smartbarber.entity.Salon;
import com.smartbarber.smartbarber.entity.Treatment;
import com.smartbarber.smartbarber.service.BarberService;
import com.smartbarber.smartbarber.service.SalonService;
import com.smartbarber.smartbarber.service.TreatmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salons")
@RequiredArgsConstructor
public class SalonController {

    // aggiungiamo i service
    private final SalonService salonService;
    private final BarberService barberService;
    private final TreatmentService treatmentService;

    // cerco i saloni per città
    @GetMapping
    public List<Salon> getSalons(@RequestParam(required = false) String city) {
        return salonService.getSalonsByCity(city);
    }

    // restituisco i barbieri che lavorano in uno specifico salone
    @GetMapping("/{id}/barbers")
    public List<Barber> getBarbersBySalon(@PathVariable Long id) {
        return barberService.getAvailableBarbersBySalon(id);
    }

    // restituisco il listino prezzi di uno specifico salone
    @GetMapping("/{id}/treatments")
    public List<Treatment> getTreatmentsBySalon(@PathVariable Long id) {
        return treatmentService.getTreatmentsBySalon(id);
    }

    // aggiungo un nuovo salone, assegnato al gestore loggato, inoltre modifico l'endpoint POST per ricevere i giorni di chiusura del salone dal frontend
    @PreAuthorize("hasAuthority('SHOP_OWNER')")
    @PostMapping
    public Salon addSalon(@RequestParam String name, @RequestParam String city, @RequestParam String address, @RequestParam(required = false) String closedDays, org.springframework.security.core.Authentication authentication) {

        String ownerKeycloakId = authentication.getName();
        return salonService.addSalon(name, city, address, closedDays, ownerKeycloakId);
    }
}