package com.smartbarber.smartbarber.controller;

import com.smartbarber.smartbarber.entity.Treatment;
import com.smartbarber.smartbarber.service.TreatmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/treatments")
@RequiredArgsConstructor
public class TreatmentController {

    private final TreatmentService treatmentService;

    // endpoint protetto per aggiungere un trattamento a listino
    @PreAuthorize("hasAuthority('SHOP_OWNER')")
    @PostMapping
    public Treatment addTreatment(@RequestParam String name, @RequestParam Double price, org.springframework.security.core.Authentication authentication) {

        String ownerKeycloakId = authentication.getName();
        return treatmentService.addTreatmentToMySalon(name, price, ownerKeycloakId);
    }
}