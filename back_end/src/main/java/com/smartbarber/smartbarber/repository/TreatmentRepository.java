package com.smartbarber.smartbarber.repository;

import com.smartbarber.smartbarber.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

    // trovo i servizi offerti da un determinato salone
    List<Treatment> findBySalonId(Long salonId);
}