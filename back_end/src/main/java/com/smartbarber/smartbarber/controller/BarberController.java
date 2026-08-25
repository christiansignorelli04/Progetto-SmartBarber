package com.smartbarber.smartbarber.controller;

import com.smartbarber.smartbarber.entity.Barber;
import com.smartbarber.smartbarber.service.BarberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize; // <-- IMPORT AGGIUNTO
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barbers")
@RequiredArgsConstructor
public class BarberController {

    private final BarberService barberService;

    // prendiamo la lista dei barbieri, non mettiamo annotazioni dato che è un metodo per tutti
    @GetMapping
    public List<Barber> getAvailableBarbers() {
        return barberService.getAvailableBarbers();
    }

    // aggiungiamo un barbiere, ed è un metodo dell'admin
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public Barber addBarber(@RequestParam String name) {
        return barberService.addBarber(name);
    }

    // richiesta per licenziare/mettere in ferie un barbiere, ed è un metodo dell'admin
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void removeBarber(@PathVariable Long id) {
        barberService.softDeleteBarber(id);
    }
}