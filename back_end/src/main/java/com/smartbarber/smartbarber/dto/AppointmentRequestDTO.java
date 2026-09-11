package com.smartbarber.smartbarber.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequestDTO {
    private Long userId;
    private Long barberId;     // l'operatore scelto
    private Long treatmentId;  // il trattamento scelto
    private LocalDate date;    // la data
    private LocalTime startTime; // l'ora esatta dello slot
}