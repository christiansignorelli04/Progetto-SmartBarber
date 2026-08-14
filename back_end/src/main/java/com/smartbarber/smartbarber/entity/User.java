package com.smartbarber.smartbarber.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // id univoco che ci fornirà Keycloak quando l'utente fa il login
    @Column(nullable = false, unique = true)
    private String keycloakId;

    @Column(nullable = false)
    private String firstName; // nome

    @Column(nullable = false)
    private String lastName; // cognome

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phoneNumber; // telefono

    @Column(nullable = false)
    private String role; // ad esempio: "CUSTOMER", "BARBER", o "ADMIN"
}
