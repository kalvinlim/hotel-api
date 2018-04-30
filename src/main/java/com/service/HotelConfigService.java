package com.service;

import com.domain.HotelConfig;
import com.exception.InvalidHotelConfigException;
import com.repository.HotelConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelConfigService {
    @Autowired
    private HotelConfigRepository hotelConfigRepository;

    public HotelConfig  getConfig() throws InvalidHotelConfigException {
        Iterable<HotelConfig> hotelConfigs = hotelConfigRepository.findAll();
        if(hotelConfigs.spliterator().getExactSizeIfKnown() != 1){
            throw new InvalidHotelConfigException("Invalid number of configurations found for this hotel");
        } else {
            return hotelConfigs.iterator().next();
        }
    }

    public void updateConfig(Integer numRooms, Integer overbookingLevel) throws InvalidHotelConfigException {
        HotelConfig hotelConfig = getConfig();
        hotelConfig.setNumRooms(numRooms);
        hotelConfig.setOverbookingLevel(overbookingLevel);
        hotelConfigRepository.save(hotelConfig);
    }

    public void createConfig(Integer numRooms, Integer overbookingLevel){
        HotelConfig hotelConfig = new HotelConfig();
        hotelConfig.setNumRooms(numRooms);
        hotelConfig.setOverbookingLevel(overbookingLevel);
        hotelConfigRepository.save(hotelConfig);
    }
}
