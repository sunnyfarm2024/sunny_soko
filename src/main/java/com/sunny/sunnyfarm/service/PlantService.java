package com.sunny.sunnyfarm.service;

import com.sunny.sunnyfarm.dto.PlantDto;
import com.sunny.sunnyfarm.dto.PlantbookDto;
import com.sunny.sunnyfarm.dto.WeatherDto;
import com.sunny.sunnyfarm.entity.UserPlant;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PlantService {
    List<PlantDto> getPlant(Integer farmId);
    List<PlantbookDto> getPlantBook(Integer userId);
    ResponseEntity<String> waterPlant(int userId, int userPlantId);
    ResponseEntity<String> sellPlant(int userId, int userPlantId);
    boolean deletePlant(int userPlantId);
    void updateGrowthStage(int userPlantId);
    UserPlant applyWeather(UserPlant userPlant, WeatherDto weatherDto);
    void addToPlantBook(int userId, int plantId);
}
