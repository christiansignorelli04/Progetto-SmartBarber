package com.smartbarber.smartbarber.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentRequestDTO {

    // dati per fare la prenotazione
    private Long userId;
    private Long barberId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}