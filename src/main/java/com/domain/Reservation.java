package com.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name="reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    @Column(columnDefinition="DATETIME")
    private LocalDate arrival;

    @Column(columnDefinition="DATETIME")
    private LocalDate departure;

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getArrival() {
        return arrival;
    }

    public LocalDate getDeparture() {
        return departure;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setArrival(LocalDate arrival) {
        this.arrival = arrival;
    }

    public void setDeparture(LocalDate departure) {
        this.departure = departure;
    }
}