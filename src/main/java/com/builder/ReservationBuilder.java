package com.builder;

import com.domain.Reservation;

import java.time.LocalDate;

public final class ReservationBuilder {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate arrival;
    private LocalDate departure;

    private ReservationBuilder() {
    }

    public static ReservationBuilder aReservation() {
        return new ReservationBuilder();
    }

    public ReservationBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public ReservationBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public ReservationBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public ReservationBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public ReservationBuilder withArrival(LocalDate arrival) {
        this.arrival = arrival;
        return this;
    }

    public ReservationBuilder withDeparture(LocalDate departure) {
        this.departure = departure;
        return this;
    }

    public Reservation build() {
        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setFirstName(firstName);
        reservation.setLastName(lastName);
        reservation.setEmail(email);
        reservation.setArrival(arrival);
        reservation.setDeparture(departure);
        return reservation;
    }
}
