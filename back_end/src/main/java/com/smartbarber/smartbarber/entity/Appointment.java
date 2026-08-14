package com.smartbarber.smartbarber.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date; // data dell'appuntamento

    @Column(nullable = false)
    private LocalDateTime startTime; // orario di inizio

    @Column(nullable = false)
    private LocalDateTime endTime; // orario di fine

    // relazioni con altre tabelle

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // il cliente che prenota

    @ManyToOne
    @JoinColumn(name = "barber_id", nullable = false)
    private Barber barber; // il barbiere prenotato
}