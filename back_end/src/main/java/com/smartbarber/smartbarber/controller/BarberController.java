package com.smartbarber.smartbarber.controller;

import com.smartbarber.smartbarber.entity.Barber;
import com.smartbarber.smartbarber.service.BarberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/barbers")
@RequiredArgsConstructor
public class BarberController {

    private final BarberService barberService;

    // endpoint protetto per assumere un barbiere
    @PreAuthorize("hasAuthority('SHOP_OWNER')")
    @PostMapping
    public Barber addBarber(@RequestParam String name, org.springframework.security.core.Authentication authentication) {
        // estraggo in modo sicuro l'ID di Keycloak
        String ownerKeycloakId = authentication.getName();
        return barberService.addBarberToMySalon(name, ownerKeycloakId);
    }
}