package com.service;

import com.builder.ReservationBuilder;
import com.domain.HotelConfig;
import com.domain.Reservation;
import com.exception.InvalidHotelConfigException;
import com.exception.RoomCapacityExceededException;
import com.repository.HotelConfigRepository;
import com.repository.ReservationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ReservationServiceTest {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private HotelConfigRepository hotelConfigRepository;

    @Autowired
    private HotelConfigService hotelConfigService;

    private final String EMAIL = "MAIL@MAIL.COM";

    @Before
    public void flushData(){
        reservationRepository.deleteAll();
        hotelConfigRepository.deleteAll();
    }

    @Test
    public void addReservation_whenNotAtCapacity_pass(){
        HotelConfig hotelConfig = new HotelConfig();
        hotelConfig.setNumRooms(1);
        hotelConfig.setOverbookingLevel(0);

        hotelConfigRepository.save(hotelConfig);

        try {
            reservationService.addReservation(buildReservation("Billy", "Joe", EMAIL, "2018-05-01","2018-05-01"));
        } catch (InvalidHotelConfigException e) {
            fail();
        } catch (RoomCapacityExceededException e) {
            fail();
        }
    }

    @Test
    public void addReservation_whenOverCapacity_expectException(){
        HotelConfig hotelConfig = new HotelConfig();
        hotelConfig.setNumRooms(1);
        hotelConfig.setOverbookingLevel(0);

        hotelConfigRepository.save(hotelConfig);

        try {
            reservationService.addReservation(buildReservation("Billy", "Joe", EMAIL, "2018-05-01","2018-05-03"));
        } catch (InvalidHotelConfigException e) {
        } catch (RoomCapacityExceededException e) {
            fail();
        }

        boolean thrown = false;

        try {
            reservationService.addReservation(buildReservation("Billy", "Joe", EMAIL, "2018-05-01","2018-05-03"));
        } catch (InvalidHotelConfigException e) {
        } catch (RoomCapacityExceededException e) {
            thrown = true;
        }

        assertTrue(thrown);
    }

    @Test
    public void addReservation_whenOverCapacityWithOverbook_expectException() throws InvalidHotelConfigException, RoomCapacityExceededException {
        HotelConfig hotelConfig = new HotelConfig();
        hotelConfig.setNumRooms(2);
        hotelConfig.setOverbookingLevel(50);

        hotelConfigRepository.save(hotelConfig);

        try {
            reservationService.addReservation(buildReservation("Billy", "Joe", EMAIL, "2018-05-01","2018-05-03"));
            reservationService.addReservation(buildReservation("Billy", "Joe", EMAIL, "2018-05-01","2018-05-03"));
            reservationService.addReservation(buildReservation("Billy", "Joe", EMAIL, "2018-05-01","2018-05-03"));
        } catch (InvalidHotelConfigException e) {
        } catch (RoomCapacityExceededException e) {
            fail();
        }

        boolean thrown = false;

        try {
            reservationService.addReservation(buildReservation("Billy", "Joe", EMAIL, "2018-05-01","2018-05-03"));
        } catch (InvalidHotelConfigException e) {
        } catch (RoomCapacityExceededException e) {
            thrown = true;
        }

        assertTrue(thrown);
    }

    @Test
    public void addReservation_whenTwoSameDayCheckInCheckOuts_noExceptionThrown() throws InvalidHotelConfigException, RoomCapacityExceededException {
        HotelConfig hotelConfig = new HotelConfig();
        hotelConfig.setNumRooms(1);
        hotelConfig.setOverbookingLevel(0);

        hotelConfigRepository.save(hotelConfig);

        reservationService.addReservation(buildReservation("Billy", "Joe", EMAIL, "2018-05-01","2018-05-03"));
        reservationService.addReservation(buildReservation("Bill", "Bob", EMAIL, "2018-05-03","2018-05-04"));
    }

    @Test
    public void whenFindAll_expectCorrectResult() throws InvalidHotelConfigException, RoomCapacityExceededException {
        HotelConfig hotelConfig = new HotelConfig();
        hotelConfig.setNumRooms(1);
        hotelConfig.setOverbookingLevel(0);

        hotelConfigRepository.save(hotelConfig);

        reservationService.addReservation(buildReservation("Billy", "Joe", EMAIL, "2018-05-01","2018-05-03"));
        reservationService.addReservation(buildReservation("Bill", "Bob", EMAIL, "2018-05-03","2018-05-04"));

        Iterable<Reservation> reservations = reservationService.findAll();

        assertEquals(2, reservations.spliterator().getExactSizeIfKnown());
    }

    public Reservation buildReservation(String firstName, String lastName, String email, String arrival, String departure){
        return ReservationBuilder.aReservation()
                .withFirstName(firstName)
                .withLastName(lastName)
                .withEmail(email)
                .withArrival(LocalDate.parse(arrival))
                .withDeparture(LocalDate.parse(departure))
                .build();
    }
}
