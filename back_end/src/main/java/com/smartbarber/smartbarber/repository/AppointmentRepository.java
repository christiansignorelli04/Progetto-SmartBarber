package com.smartbarber.smartbarber.repository;

import com.smartbarber.smartbarber.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.barber.id = :barberId " +
            "AND a.date = :date " +
            "AND (a.startTime < :endTime AND a.endTime > :startTime)")
    boolean checkOverbooking(
            @Param("barberId") Long barberId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    // restituisco tutte le prenotazioni di un utente come cliente
    List<Appointment> findByUserId(Long userId);

    // conto quanti appuntamenti ci sono per il salone di questo proprietario
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.barber.salon.owner.keycloakId = :ownerKeycloakId")
    long countAppointmentsBySalonOwner(@Param("ownerKeycloakId") String ownerKeycloakId);

    // trovo gli appuntamenti per nascondere gli orari occupati
    List<Appointment> findByBarberIdAndDate(Long barberId, LocalDate date);

    // trovo le prenotazioni per il calendario del gestore
    List<Appointment> findByBarberSalonOwnerKeycloakIdOrderByDateAscStartTimeAsc(String keycloakId);
}