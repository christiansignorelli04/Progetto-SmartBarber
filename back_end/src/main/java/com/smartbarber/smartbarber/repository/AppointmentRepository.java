package com.smartbarber.smartbarber.repository;

import com.smartbarber.smartbarber.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // query per controllare se esiste già un appuntamento che si sovrappone agli orari richiesti
    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.barber.id = :barberId " +
            "AND (a.startTime < :endTime AND a.endTime > :startTime)")
    boolean checkOverbooking(
            @Param("barberId") Long barberId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}