package com.smartbarber.smartbarber.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "barbers")
public class Barber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean available = true; // di default è disponibile

    private boolean isActive = true; // di default un barbiere è attivo quando lo si assume

    // relazione dove ogni barbiere lavora in un salone
    @ManyToOne
    @JoinColumn(name = "salon_id", nullable = false)
    private Salon salon;
}