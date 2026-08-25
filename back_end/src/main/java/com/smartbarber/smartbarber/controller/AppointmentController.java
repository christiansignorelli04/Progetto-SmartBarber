package com.smartbarber.smartbarber.controller;

import com.smartbarber.smartbarber.dto.AppointmentRequestDTO;
import com.smartbarber.smartbarber.entity.Appointment;
import com.smartbarber.smartbarber.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentRequestDTO request) {

        // passiamo i dati estratti dal DTO al Service
        Appointment newAppointment = appointmentService.createAppointment(request.getUserId(), request.getBarberId(), request.getStartTime(), request.getEndTime());

        // restituiamo al sito web l'esito positivo con il codice HTTP 201 (Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(newAppointment);
    }
}