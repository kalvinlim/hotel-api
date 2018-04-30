package com.repository;

import com.domain.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    @Query(value=  "SELECT * FROM hotel_db.reservations WHERE (arrival <= ?1 AND departure >= ?1) OR (arrival >= ?1 AND arrival <= ?2)", nativeQuery = true)
    List<Reservation> getAllReservationsInRange(LocalDate arrival, LocalDate departure);
}