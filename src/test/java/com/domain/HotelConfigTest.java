package com.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HotelConfigTest {

    @Test
    public void testMaxCapacity(){
        HotelConfig hotelConfig = new HotelConfig();

        hotelConfig.setNumRooms(100);
        hotelConfig.setOverbookingLevel(10);

        assertEquals(110, hotelConfig.getMaxCapacity());

        hotelConfig.setNumRooms(100);
        hotelConfig.setOverbookingLevel(0);

        assertEquals(100, hotelConfig.getMaxCapacity());

        hotelConfig.setNumRooms(200);
        hotelConfig.setOverbookingLevel(25);

        assertEquals(250, hotelConfig.getMaxCapacity());

        hotelConfig.setNumRooms(100);
        hotelConfig.setOverbookingLevel(200);

        assertEquals(300, hotelConfig.getMaxCapacity());
    }
}
