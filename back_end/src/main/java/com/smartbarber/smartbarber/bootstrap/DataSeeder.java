package com.smartbarber.smartbarber.bootstrap;

import com.smartbarber.smartbarber.entity.Barber;
import com.smartbarber.smartbarber.entity.Salon;
import com.smartbarber.smartbarber.entity.Treatment;
import com.smartbarber.smartbarber.repository.BarberRepository;
import com.smartbarber.smartbarber.repository.SalonRepository;
import com.smartbarber.smartbarber.repository.TreatmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final SalonRepository salonRepository;
    private final BarberRepository barberRepository;
    private final TreatmentRepository treatmentRepository;

    @Override
    public void run(String... args) throws Exception {
        if (salonRepository.count() == 0) {
            System.out.println("Database vuoto. Popolamento con la nuova struttura in corso...");

            //  creiamo i saloni di default con città e indirizzo
            Salon salonRoma1 = new Salon();
            salonRoma1.setName("Barberia Roma Centro");
            salonRoma1.setCity("roma");
            salonRoma1.setAddress("Via del Corso, 45");

            Salon salonMilano1 = new Salon();
            salonMilano1.setName("Milano Style Barber");
            salonMilano1.setCity("milano");
            salonMilano1.setAddress("Via Monte Napoleone, 8");

            salonRepository.saveAll(List.of(salonRoma1, salonMilano1));

            // creiamo i barbieri e li colleghiamo ai saloni
            Barber barber1 = new Barber();
            barber1.setName("Luigi Voci");
            barber1.setAvailable(true);
            barber1.setSalon(salonRoma1);

            Barber barber2 = new Barber();
            barber2.setName("Alessandro Ceraudo");
            barber2.setAvailable(true);
            barber2.setSalon(salonRoma1);

            Barber barber3 = new Barber();
            barber3.setName("Marco Procopio");
            barber3.setAvailable(true);
            barber3.setSalon(salonMilano1);

            barberRepository.saveAll(List.of(barber1, barber2, barber3));

            // creiamo il trattamento con listino prezzi per i saloni
            Treatment t1 = new Treatment();
            t1.setName("Taglio Capelli");
            t1.setPrice(15.00);
            t1.setSalon(salonRoma1);

            Treatment t2 = new Treatment();
            t2.setName("Barba");
            t2.setPrice(8.00);
            t2.setSalon(salonRoma1);

            Treatment tComboRoma = new Treatment();
            tComboRoma.setName("Taglio + Barba Completo");
            tComboRoma.setPrice(22.00);
            tComboRoma.setSalon(salonRoma1);

            Treatment t3 = new Treatment();
            t3.setName("Taglio + Barba Completo");
            t3.setPrice(25.00);
            t3.setSalon(salonMilano1);

            treatmentRepository.saveAll(List.of(t1, t2, tComboRoma, t3));

            System.out.println("Database popolato con successo con Saloni, Operatori e Trattamenti!");
        } else {
            System.out.println("Il database contiene già dei dati.");
        }
    }
}