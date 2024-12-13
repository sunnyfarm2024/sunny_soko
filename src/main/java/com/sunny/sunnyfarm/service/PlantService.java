package com.sunny.sunnyfarm.service;

import com.sunny.sunnyfarm.dto.UserPlantDto;
import com.sunny.sunnyfarm.dto.WeatherDto;
import com.sunny.sunnyfarm.entity.UserPlant;
import org.springframework.http.ResponseEntity;

public interface PlantService {
    UserPlantDto getPlantInfo(int userPlantId);
    ResponseEntity<String> waterPlant(int userId, int userPlantId);
    ResponseEntity<String> sellPlant(int userId, int userPlantId);
    boolean deletePlant(int userPlantId);
    void updateGrowthStage(int userPlantId);
    UserPlant applyWeather(UserPlant userPlant, WeatherDto weatherDto);
    void addToPlantBook(int userId, int plantId);

}
