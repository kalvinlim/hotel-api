package com.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="hotel_config")
public class HotelConfig {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private Integer numRooms;

    private Integer overbookingLevel;

    public Integer getId() {
        return id;
    }

    public Integer getNumRooms() {
        return numRooms;
    }

    public Integer getOverbookingLevel() {
        return overbookingLevel;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNumRooms(Integer numRooms) {
        this.numRooms = numRooms;
    }

    public void setOverbookingLevel(Integer overbookingLevel) {
        this.overbookingLevel = overbookingLevel;
    }

    public int getMaxCapacity(){

        return(int)(numRooms * (1+((double)overbookingLevel/100)));
    }
}