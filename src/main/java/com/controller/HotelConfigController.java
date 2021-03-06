package com.controller;

import com.domain.HotelConfig;
import com.exception.InvalidHotelConfigException;
import com.service.HotelConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/rest/config")
public class HotelConfigController {
    @Autowired
    private HotelConfigService hotelConfigService;

    @GetMapping(path="/get")
    public @ResponseBody HotelConfig getConfig() throws InvalidHotelConfigException {
        return hotelConfigService.getConfig();
    }

    @PostMapping(path="/update")
    public @ResponseBody String updateConfig(@RequestParam Integer numRooms, @RequestParam Integer overbookingLevel) throws InvalidHotelConfigException {
        hotelConfigService.updateConfig(numRooms, overbookingLevel);
        return "Success";
    }

    @PostMapping(path="/create")
    public @ResponseBody String createConfig(@RequestParam Integer numRooms, @RequestParam Integer overbookingLevel) throws InvalidHotelConfigException {
        hotelConfigService.createConfig(numRooms, overbookingLevel);
        return "Success";
    }
}
