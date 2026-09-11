package com.smartbarber.smartbarber.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

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
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    // relazioni
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // il cliente che prenota

    @ManyToOne
    @JoinColumn(name = "barber_id", nullable = false)
    private Barber barber; // l'operatore specifico scelto

    @ManyToOne
    @JoinColumn(name = "treatment_id", nullable = false)
    private Treatment treatment; // tipo di trattamento
}