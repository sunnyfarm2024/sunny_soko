package com.sunny.sunnyfarm.service;

import com.sunny.sunnyfarm.dto.UserPlantDto;
import com.sunny.sunnyfarm.dto.WeatherDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public interface PlantService {
    UserPlantDto getPlantInfo(int userPlantId);
    ResponseEntity<String> waterPlant(int userId, int userPlantId);
    ResponseEntity<String> sellPlant(int userId, int userPlantId);
    boolean deletePlant(int userPlantId);
    void updateGrowthStage(int userPlantId);
    void applyWeather(int userPlantId, WeatherDto weather);
    void applyFertilizer(int userPlantId, LocalDateTime fertilizerEndsAt);
    void applyGnome(int userPlantId);
    void applyDecoration(int userPlantId);
    void addToPlantBook(int userId, int plantId);
}
