package com.service;

import com.domain.HotelConfig;
import com.exception.InvalidHotelConfigException;
import com.repository.HotelConfigRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class HotelConfigServiceTest {

    @Autowired
    private HotelConfigService hotelConfigService;

    @Autowired
    private HotelConfigRepository hotelConfigRepository;

    @Before
    public void clearConfigs(){
        hotelConfigRepository.deleteAll();
    }
    @Test(expected = InvalidHotelConfigException.class)
    public void whenNoConfig_expectInvalidHotelConfigException() throws InvalidHotelConfigException {
        hotelConfigService.getConfig();
    }

    @Test(expected = InvalidHotelConfigException.class)
    public void whenMultipleConfig_expectInvalidHotelConfigException() throws InvalidHotelConfigException {
        HotelConfig hotelConfig1 = new HotelConfig();
        hotelConfig1.setNumRooms(100);
        hotelConfig1.setOverbookingLevel(10);

        HotelConfig hotelConfig2 = new HotelConfig();
        hotelConfig2.setNumRooms(1000);
        hotelConfig2.setOverbookingLevel(10);

        hotelConfigRepository.save(hotelConfig1);
        hotelConfigRepository.save(hotelConfig2);

        hotelConfigService.getConfig();
    }

    @Test
    public void whenUpdate_expectValuesUpdated(){
        final Integer NUM_ROOMS = 300;
        final Integer OVERBOOKING_LEVEL = 11;

        HotelConfig hotelConfig = new HotelConfig();
        hotelConfig.setNumRooms(100);
        hotelConfig.setOverbookingLevel(10);

        hotelConfigRepository.save(hotelConfig);

        try {
            hotelConfigService.updateConfig(NUM_ROOMS, OVERBOOKING_LEVEL);
            HotelConfig result = hotelConfigService.getConfig();

            assertEquals(NUM_ROOMS, result.getNumRooms());
            assertEquals(OVERBOOKING_LEVEL, result.getOverbookingLevel());
        } catch (InvalidHotelConfigException e) {
            fail();
        }
    }
}
