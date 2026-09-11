package com.smartbarber.smartbarber.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "treatments")
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // "Taglio", "Barba", "Taglio + Barba"

    @Column(nullable = false)
    private double price; //  15.00, 8.00, 18.00

    // ogni trattamento appartiene al listino di un salone
    @ManyToOne
    @JoinColumn(name = "salon_id", nullable = false)
    private Salon salon;
}