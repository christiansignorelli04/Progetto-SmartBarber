package com.smartbarber.smartbarber.repository;

import com.smartbarber.smartbarber.entity.Barber;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BarberRepository extends JpaRepository<Barber, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Barber b WHERE b.id = :id")
    Optional<Barber> findByIdWithLock(@Param("id") Long id);

    // trovo tutti i dipendenti di un determinato salone
    List<Barber> findBySalonId(Long salonId);

    // trovo tutti i dipendenti ATTIVI di un determinato salone (fondamentale per nascondere i licenziati ai clienti)
    List<Barber> findBySalonIdAndIsActiveTrue(Long salonId);

    // trovo tutti i barbieri attivi in generale
    List<Barber> findAllByIsActiveTrue();
}