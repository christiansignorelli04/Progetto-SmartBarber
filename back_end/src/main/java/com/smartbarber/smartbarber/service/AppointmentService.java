package com.smartbarber.smartbarber.service;

import com.smartbarber.smartbarber.entity.Appointment;
import com.smartbarber.smartbarber.entity.User;
import com.smartbarber.smartbarber.repository.AppointmentRepository;
import com.smartbarber.smartbarber.repository.BarberRepository;
import com.smartbarber.smartbarber.repository.TreatmentRepository;
import com.smartbarber.smartbarber.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final BarberRepository barberRepository;
    private final UserRepository userRepository;
    private final TreatmentRepository treatmentRepository;

    @Transactional
    public Appointment createAppointment(Long userId, Long barberId, Long treatmentId, LocalDate date, LocalTime startTime) {
        var user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Utente non trovato"));
        var barber = barberRepository.findByIdWithLock(barberId).orElseThrow(() -> new RuntimeException("Barbiere non trovato"));
        var treatment = treatmentRepository.findById(treatmentId).orElseThrow(() -> new RuntimeException("Trattamento non trovato"));

        // controllo date passate
        LocalDate today = LocalDate.now();
        if (date.isBefore(today)) {
            throw new RuntimeException("Non puoi prenotare in una data passata!");
        }
        if (date.isEqual(today) && startTime.isBefore(LocalTime.now())) {
            throw new RuntimeException("Questo orario è già passato!");
        }

        // controllo festività fisse
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();
        boolean isHoliday = (day == 1 && month == 1) || (day == 6 && month == 1) ||
                (day == 25 && month == 4) || (day == 1 && month == 5) ||
                (day == 2 && month == 6) || (day == 15 && month == 8) ||
                (day == 1 && month == 11) || (day == 8 && month == 12) ||
                (day == 25 && month == 12) || (day == 26 && month == 12);
        if (isHoliday) {
            throw new RuntimeException("Il salone è chiuso in questa data per festività.");
        }

        // controllo giorni di chiusura settimanali del salone
        String closedDays = barber.getSalon().getClosedDays();
        if (closedDays != null && !closedDays.isEmpty()) {
            int javaDay = date.getDayOfWeek().getValue(); // LUN=1, DOM=7
            int jsDay = (javaDay == 7) ? 0 : javaDay; // allineo la domenica a 0
            if (closedDays.contains(String.valueOf(jsDay))) {
                throw new RuntimeException("Il salone è chiuso nel giorno della settimana selezionato.");
            }
        }

        LocalTime endTime = startTime.plusMinutes(30);

        List<Appointment> barberAppointmentsForDay = appointmentRepository.findAll().stream()
                .filter(app -> app.getBarber().getId().equals(barberId) && app.getDate().equals(date))
                .toList();

        for (Appointment existingApp : barberAppointmentsForDay) {
            LocalTime existingStart = existingApp.getStartTime();
            LocalTime existingEnd = existingApp.getEndTime();

            if (startTime.isBefore(existingEnd) && endTime.isAfter(existingStart)) {
                throw new RuntimeException("Attenzione: L'operatore " + barber.getName() + " è già prenotato in questo orario!");
            }
        }

        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setBarber(barber);
        appointment.setTreatment(treatment);
        appointment.setDate(date);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAllAppointments() { return appointmentRepository.findAll(); }

    public long getTotalAppointmentsCountByOwner(String ownerKeycloakId) {
        return appointmentRepository.countAppointmentsBySalonOwner(ownerKeycloakId);
    }

    public List<Appointment> getAppointmentsByOwner(String ownerKeycloakId) {
        return appointmentRepository.findByBarberSalonOwnerKeycloakIdOrderByDateAscStartTimeAsc(ownerKeycloakId);
    }

    @Transactional
    public void cancelAppointment(Long appointmentId, String keycloakUserId) {
        Appointment app = appointmentRepository.findById(appointmentId).orElseThrow(() -> new RuntimeException("Appuntamento non trovato"));
        if (!app.getUser().getKeycloakId().equals(keycloakUserId)) { throw new RuntimeException("Non sei autorizzato a cancellare questo appuntamento."); }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime appointmentDateTime = LocalDateTime.of(app.getDate(), app.getStartTime());
        if (now.plusHours(2).isAfter(appointmentDateTime)) { throw new RuntimeException("Troppo tardi! Puoi disdire un appuntamento solo fino a 2 ore prima."); }
        appointmentRepository.delete(app);
    }

    public List<Appointment> getMyCustomerAppointments(String keycloakId) {
        User user = userRepository.findAll().stream()
                .filter(u -> u.getKeycloakId().equals(keycloakId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        return appointmentRepository.findByUserId(user.getId());
    }

    public List<LocalTime> getBookedTimes(Long barberId, LocalDate date) {
        return appointmentRepository.findByBarberIdAndDate(barberId, date)
                .stream()
                .map(Appointment::getStartTime)
                .toList();
    }
}