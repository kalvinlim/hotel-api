package com.service;

import com.domain.HotelConfig;
import com.domain.Reservation;
import com.exception.InvalidHotelConfigException;
import com.exception.RoomCapacityExceededException;
import com.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private HotelConfigService hotelConfigService;

    public void addReservation(Reservation reservation) throws InvalidHotelConfigException, RoomCapacityExceededException {
        HotelConfig hotelConfig = hotelConfigService.getConfig();

        List<Reservation> reservationsInRange = reservationRepository.getAllReservationsInRange(reservation.getArrival(), reservation.getDeparture());

        Map<LocalDate, Integer> roomsBookedWithinWindow = countRoomsBookedWithinWindow(reservationsInRange, reservation.getArrival(), reservation.getDeparture());

        if(allReservationDatesHaveCapacity(roomsBookedWithinWindow, hotelConfig.getMaxCapacity())){
            reservationRepository.save(reservation);
        } else {
            throw new RoomCapacityExceededException("Rooms are at capacity for one or more dates requested");
        }
    }

    private Map<LocalDate, Integer> countRoomsBookedWithinWindow(List<Reservation> reservations, LocalDate arrival, LocalDate departure){
        Map<LocalDate, Integer> result = new HashMap<>();
        reservations.stream().forEach(res -> {
            for (LocalDate date = res.getArrival(); date.isBefore(res.getDeparture()); date = date.plusDays(1)){
                if(isDateWithinWindow(date, arrival, departure)){
                    result.put(date, result.getOrDefault(date, 0)+1);
                }
            }
        });
        return result;
    }

    private boolean allReservationDatesHaveCapacity(Map<LocalDate, Integer> roomsBookedForReservationRequest, int maxCapacity){
        boolean result = true;
        for(int capacity : roomsBookedForReservationRequest.values()){
            if(capacity >= maxCapacity){
                result = false;
            }
        }
        return result;
    }

    private boolean isDateWithinWindow(LocalDate date, LocalDate arrival, LocalDate departure){
        return !(date.isBefore(arrival) || date.isAfter(departure));
    }

}
