package com.smartbarber.smartbarber.service;

import com.smartbarber.smartbarber.entity.Appointment;
import com.smartbarber.smartbarber.repository.AppointmentRepository;
import com.smartbarber.smartbarber.repository.BarberRepository;
import com.smartbarber.smartbarber.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final BarberRepository barberRepository;
    private final UserRepository userRepository;

    @Transactional
    public Appointment createAppointment(Long userId, Long barberId, LocalDateTime startTime, LocalDateTime endTime) {

        // controlliamo l'overbooking
        boolean isOccupied = appointmentRepository.checkOverbooking(barberId, startTime, endTime);
        if (isOccupied) {
            // se c'è già qualcuno fermiamo tutto
            throw new RuntimeException("Attenzione: Il barbiere selezionato è già prenotato in questo orario!");
        }

        // recuperiamo l'utente e il barbiere dal database
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        var barber = barberRepository.findById(barberId)
                .orElseThrow(() -> new RuntimeException("Barbiere non trovato"));

        // creiamo l'appuntamento e lo leghiamo
        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setBarber(barber);
        appointment.setDate(startTime.toLocalDate());
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);

        // salviamo nel database
        return appointmentRepository.save(appointment);
    }
}