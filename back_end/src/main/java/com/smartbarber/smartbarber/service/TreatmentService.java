package com.smartbarber.smartbarber.service;

import com.smartbarber.smartbarber.entity.Treatment;
import com.smartbarber.smartbarber.entity.Salon;
import com.smartbarber.smartbarber.repository.TreatmentRepository;
import com.smartbarber.smartbarber.repository.SalonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final SalonRepository salonRepository; // lo uso per trovare il salone del gestore

    public List<Treatment> getTreatmentsBySalon(Long salonId) {
        return treatmentRepository.findBySalonId(salonId);
    }

    // aggiungo un trattamento al salone del gestore loggato
    @Transactional
    public Treatment addTreatmentToMySalon(String name, Double price, String ownerKeycloakId) {

        Salon mySalon = salonRepository.findByOwnerKeycloakId(ownerKeycloakId)
                .orElseThrow(() -> new RuntimeException("Devi prima creare il tuo salone nell'Area Business prima di inserire il listino prezzi!"));

        Treatment treatment = new Treatment();
        treatment.setName(name);
        treatment.setPrice(price);
        treatment.setSalon(mySalon);
        return treatmentRepository.save(treatment);
    }
}