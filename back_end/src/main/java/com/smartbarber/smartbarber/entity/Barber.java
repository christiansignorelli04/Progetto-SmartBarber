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
    private String name; // nome del barbiere

    @Column(nullable = false)
    private boolean available; // true se lavora, false se è in ferie/malattia

    public void occupyBarber() {
        this.available = false;
    }

    public void freeBarber() {
        this.available = true;
    }
}
