package com.sunny.sunnyfarm.service.impl;
import com.sunny.sunnyfarm.dto.UserPlantDto;
import com.sunny.sunnyfarm.dto.WeatherDto;
import com.sunny.sunnyfarm.entity.Farm;
import com.sunny.sunnyfarm.entity.Plant;
import com.sunny.sunnyfarm.entity.User;
import com.sunny.sunnyfarm.entity.UserPlant;
import com.sunny.sunnyfarm.repository.PlantRepository;
import com.sunny.sunnyfarm.repository.UserPlantRepository;
import com.sunny.sunnyfarm.repository.UserRepository;
import com.sunny.sunnyfarm.service.PlantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PlantServiceImpl implements PlantService {

    private final UserPlantRepository userPlantRepository;
    private final UserRepository userRepository;
    private final PlantRepository plantRepository;

    public PlantServiceImpl(UserPlantRepository userPlantRepository, UserRepository userRepository, PlantRepository plantRepository) {
        this.userPlantRepository = userPlantRepository;
        this.userRepository = userRepository;
        this.plantRepository = plantRepository;
    }

    @Override
    public UserPlantDto getPlantInfo(int userPlantId) {
        return null;
    }


    @Override
    public ResponseEntity<String> waterPlant(int userId, int userPlantId) {
        UserPlant userPlant = userPlantRepository.findByUserPlantId(userPlantId);
        User user = userRepository.getById(userId);

        int waterLevel = userPlant.getWaterLevel();
        int waterBalance = user.getWaterBalance();

        if (waterLevel < 5) {
            if (waterBalance > 0) {
                waterBalance--;
                waterLevel++;
                user.setWaterBalance(waterBalance);
                userPlant.setWaterLevel(waterLevel);
                userPlant.setLastWateredAt(LocalDateTime.now());

                userRepository.save(user);
                userPlantRepository.save(userPlant);

                return ResponseEntity.ok("물을 성공적으로 주었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("물이 부족합니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("물이 이미 꽉 찼습니다.");
        }
    }


    @Override
    public ResponseEntity<String> sellPlant(int userId, int userPlantId) {
        UserPlant userPlant = userPlantRepository.findByUserPlantId(userPlantId);
        User user = userRepository.getById(userId);
        Plant plant = plantRepository.getById(userPlantId);

        if (userPlant.getGrowthStage() == UserPlant.GrowthStage.MAX) {
            if (deletePlant(userPlantId)) {
                int price = plant.getSalePrice();
                int coinBalance = user.getCoinBalance();
                user.setCoinBalance(price + coinBalance);
                userPlantRepository.save(userPlant);
                //퀘스트 달성
                return ResponseEntity.ok("식물을 판매했습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("판매 실패했습니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("아직 팔 수 없습니다.");
        }
    }

    @Override
    public boolean deletePlant(int userPlantId) {
        try {
            userPlantRepository.deleteById(userPlantId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void updateGrowthStage(int userPlantId) {
        UserPlant userPlant = userPlantRepository.getUserPlants(userPlantId);
        Farm farm = userPlant.getFarm();
        Plant plant = userPlant.getPlant();
        User user = userPlant.getFarm().getUser();

        float growthProgress = userPlant.getGrowthProgress();

        //날씨 호출 ~ Weather Dto 반환 ....... 했다 치고..
        WeatherDto weatherDto = new WeatherDto(
                "7",
                "1",
                "0",
                "35",
                "1",
                "0"
        );

        applyWeather(userPlantId, weatherDto);
        //해서 식물이 살아남았다 -> 반환 타입을 바꾸자
        //해서 식물이 죽었따 -> 끝

        applyFertilizer(userPlantId, userPlant.getFertilizerEndsAt());

        /*식물 성장
        : 각 난이도 별 일조시간 20, 30, 40 시간
          일조 시간의 34%까지 1레벨, 67%까지 2레벨, 100%까지 3레벨, 그 이후 MAX
        */

    }

    @Override
    public void applyWeather(int userPlantId, WeatherDto weatherDto) {
        System.out.println("Temperature: " + weatherDto.getTemperature());
        System.out.println("Sky Status: " + weatherDto.getSkyStatus());
        System.out.println("Precipitation: " + weatherDto.getPrecipitationType());
        System.out.println("Humidity: " + weatherDto.getHumidity());
        System.out.println("Wind Speed: " + weatherDto.getWindSpeed());
        System.out.println("Lightning: " + weatherDto.getLightning());

        float growthProgress = 0;

        //1 맑, 3 구 많, 4 흐림
        //강수량 0 없, 1 비, 2 비눈, -> x 3 눈, 5 빗방울, 6 빗방울 눈날림, 7 눈날림
        if (weatherDto.getSkyStatus() == "1") {}

        /*날씨 조건별 영향:
        - 맑은 날: 성장 속도 +10% 가속
                - 비 오는 날: 물이 없어도 성장 속도 유지
                - 적절한 습도 (50~70): 성장 속도 +5% 가속
                - 적정온도 (15~25): 성장 속도 +5% 가속
                - 낙뢰:
        30kA 미만 - 피해 없음
        30 ~ 50kA - 5% 확률로 식물 사망
        50 ~ 100kA - 10% 확률로 식물 사망
        100kA 이상 - 15% 확률로 식물 사망

        - 풍속:
        10m/s 미만 - 피해 없음
        10 ~ 20m/s - 5% 확률로 식물 사망
        20 ~ 30m/s - 10% 확률로 식물 사망
        30m/s 이상 - 15% 확률로 식물 사망*/
    }

    @Override
    public void applyFertilizer(int userPlantId, LocalDateTime fertilizerEndsAt) {
        //영양제가 있으면 +10%
    }

    @Override
    public void applyGnome(int userPlantId) {
        //놈 끝나는 시간이 지금보다 뒤면
        //물 항상 5
        //낙뢰, 강풍 피해 방지
    }

    @Override
    public void applyDecoration(int userPlantId) {
        //corner에 24번 아이템 사용중이면 +1%
        //corner에 24번 아이템 사용중이면 +3%
    }

    @Override
    public void addToPlantBook(int userId, int plantId) {
        //식물이 MAX가 되면 식물도감에 추가
    }
}
