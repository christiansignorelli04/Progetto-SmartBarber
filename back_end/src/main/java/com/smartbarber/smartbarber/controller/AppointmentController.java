package com.smartbarber.smartbarber.controller;

import com.smartbarber.smartbarber.dto.AppointmentRequestDTO;
import com.smartbarber.smartbarber.entity.Appointment;
import com.smartbarber.smartbarber.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentRequestDTO request) {
        Appointment newAppointment = appointmentService.createAppointment(
                request.getUserId(), request.getBarberId(), request.getTreatmentId(), request.getDate(), request.getStartTime()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(newAppointment);
    }

    @PreAuthorize("hasAuthority('SHOP_OWNER')")
    @GetMapping("/count")
    public ResponseEntity<Long> getAppointmentsCount(org.springframework.security.core.Authentication authentication) {
        String ownerKeycloakId = authentication.getName();
        long count = appointmentService.getTotalAppointmentsCountByOwner(ownerKeycloakId);
        return ResponseEntity.ok(count);
    }

    @PreAuthorize("hasAuthority('SHOP_OWNER')")
    @GetMapping("/my-salon")
    public ResponseEntity<List<Appointment>> getMySalonAppointments(org.springframework.security.core.Authentication authentication) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByOwner(authentication.getName()));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id, org.springframework.security.core.Authentication authentication) {
        String keycloakUserId = authentication.getName();
        appointmentService.cancelAppointment(id, keycloakUserId);
        return ResponseEntity.noContent().build();
    }

    // endpoint per le prenotazioni del cliente
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my-bookings")
    public ResponseEntity<List<Appointment>> getMyCustomerAppointments(org.springframework.security.core.Authentication authentication) {
        String keycloakUserId = authentication.getName();
        return ResponseEntity.ok(appointmentService.getMyCustomerAppointments(keycloakUserId));
    }

    // endpoint per chiedere quali orari nascondere ai clienti
    @GetMapping("/booked-times")
    public ResponseEntity<List<LocalTime>> getBookedTimes(
            @RequestParam Long barberId,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(appointmentService.getBookedTimes(barberId, date));
    }
}