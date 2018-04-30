package com.controller;

import com.builder.ReservationBuilder;
import com.domain.Reservation;
import com.exception.InvalidHotelConfigException;
import com.exception.RoomCapacityExceededException;
import com.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping(path="/rest/reservation") // This means URL's start with /demo (after com.Application path)
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @GetMapping(path="get/all")
    public @ResponseBody Iterable<Reservation> getAllUsers(){
        return reservationService.findAll();
    }

    @PostMapping(path="/save")
    public @ResponseBody String saveReservation(@RequestParam String firstName,
                                                @RequestParam String lastName,
                                                @RequestParam String email,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate arrival,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departure) throws InvalidHotelConfigException, RoomCapacityExceededException {

        Reservation reservation = ReservationBuilder.aReservation()
                                                    .withFirstName(firstName)
                                                    .withLastName(lastName)
                                                    .withEmail(email)
                                                    .withArrival(arrival)
                                                    .withDeparture(departure)
                                                    .build();

        reservationService.addReservation(reservation);

        return "Success";
    }
}
