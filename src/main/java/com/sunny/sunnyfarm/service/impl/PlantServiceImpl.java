package com.sunny.sunnyfarm.service.impl;

import com.sunny.sunnyfarm.dto.PlantDto;
import com.sunny.sunnyfarm.dto.PlantbookDto;
import com.sunny.sunnyfarm.dto.WeatherDto;
import com.sunny.sunnyfarm.entity.*;
import com.sunny.sunnyfarm.repository.*;
import com.sunny.sunnyfarm.service.PlantService;
import com.sunny.sunnyfarm.service.QuestService;
import com.sunny.sunnyfarm.service.TitleService;
import com.sunny.sunnyfarm.service.WeatherServise;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PlantServiceImpl implements PlantService {

    private final FarmRepository farmRepository;
    private final PlantRepository plantRepository;
    private final TitleService titleService;
    private final UserPlantRepository userPlantRepository;
    private final UserRepository userRepository;
    private final PlantBookRepository plantbookRepository;
    private final TitleRepository titleRepository;
    private final WeatherServise weatherServise;
    private final QuestRepository questRepository;
    private final QuestService questService;
    private final UserQuestRepository userQuestRepository;

    public List<PlantDto> getPlant(Integer farmId){
        Farm farm = farmRepository.findById(farmId)
                .orElseThrow(() -> new IllegalArgumentException("농장을 찾을 수 없습니다. ID: " + farmId));

        List<UserPlant> userPlants = Stream.of(
                farm.getLeftPlant(),
                farm.getCenterPlant(),
                farm.getRightPlant()
        ).filter(Objects::nonNull).toList(); // Null 값 제외

        List<PlantDto> plantDtos = new ArrayList<>();

        for (UserPlant userPlant : userPlants) {
            Plant plant = plantRepository.findById(userPlant.getPlant().getPlantId())
                    .orElseThrow(() -> new IllegalArgumentException("식물을 찾을 수 없습니다. ID: " + userPlant.getPlant().getPlantId()));

            String plantLocation = findPlantLocation(farm, userPlant);
            String plantImage = findPlantImage(userPlant, plant);

            PlantDto plantDto = new PlantDto(
                    userPlant.getPlantName(),
                    plant.getPlantType().name(),
                    userPlant.getGrowthStage().name(),
                    String.valueOf(userPlant.getGrowthProgress()),
                    userPlant.getWaterLevel(),
                    userPlant.getLivesLeft(),
                    plantLocation,
                    plantImage,
                    plant.getDifficulty().name(),
                    userPlant.getFertilizerEndsAt()
            );
            plantDtos.add(plantDto);
        }
        return plantDtos;
    }

    @Override
    public List<PlantbookDto> getPlantBook(Integer userId) {
        List<PlantBook> plantBooks = plantbookRepository.getByUserId(userId);

        return plantBooks.stream()
                .map(plantBook -> new PlantbookDto(
                        plantBook.getPlantbookDescription(),
                        plantBook.getPlantbookImage()
                )).toList();
    }

    private String findPlantLocation(Farm farm, UserPlant userPlant) {
        if (userPlant == farm.getLeftPlant()) {
            return "left";
        } else if (userPlant == farm.getCenterPlant()) {
            return "center";
        } else if (userPlant == farm.getRightPlant()) {
            return "right";
        }
        return null;
    }

    private String findPlantImage(UserPlant userPlant, Plant plant) {
        if (userPlant.getLivesLeft() == 0) {
            return plant.getDeadImage();
        } else {
            return switch (userPlant.getGrowthStage()) {
                case LEVEL1 -> plant.getLevel1Image();
                case LEVEL2 -> plant.getLevel2Image();
                case LEVEL3 -> plant.getLevel3Image();
                case MAX -> plant.getMaxImage();
            };
        }
    }

    @Override
    public ResponseEntity<String> waterPlant(int userId, int userPlantId) {
        UserPlant userPlant = userPlantRepository.findByUserPlantId(userPlantId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수가 없습니다"));
        int waterLevel = userPlant.getWaterLevel();
        int waterBalance = user.getWaterBalance();

        LocalDateTime now = LocalDateTime.now();

        if (waterLevel < 5) {
            if (waterBalance > 0) {
                waterBalance--;
                waterLevel++;
                user.setWaterBalance(waterBalance);
                userPlant.setWaterLevel(waterLevel);
                userPlant.setLastWateredAt(now);

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
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수가 없습니다"));

        int plantId = userPlant.getPlant().getPlantId();
        Plant plant = plantRepository.findById(plantId).orElseThrow(() -> new EntityNotFoundException("해당 식물을 찾을 수 없습니다"));

        if (userPlant.getGrowthStage() == UserPlant.GrowthStage.MAX) {
            if (deletePlant(userPlantId)) {
                int price = plant.getSalePrice();
                int coinBalance = user.getCoinBalance();
                user.setCoinBalance(price + coinBalance);
                userRepository.save(user);
                questService.updateQuestProgress(userId, plantId);
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

    @Override
    public void updateGrowthStage(int userPlantId) {

        UserPlant userPlant = userPlantRepository.getUserPlants(userPlantId);
        Farm farm = userPlant.getFarm();
        Plant plant = userPlant.getPlant();
        User user = userPlant.getFarm().getUser();

        //WeatherDto weatherDto = weatherServise.getWeather(user.getUserId());

        WeatherDto weatherDto = new WeatherDto(
                "19",
                "1",
                "0",
                "58",
                "0",
                "1"
        );


        LocalDateTime now = LocalDateTime.now();

        LocalDateTime gnomeEndsAt = farm.getGnomeEndsAt();
        LocalDateTime lastWateredAt = userPlant.getLastWateredAt(); //

        float sunlightHours;

        if (userPlant.getSunlightHours() == 0) { //방금 심은 식물이라면
            sunlightHours = (ChronoUnit.MINUTES.between(lastWateredAt, now) + 1) / 60F;
        } else {
            sunlightHours = 0.5F; // 30분
        }


        int livesLeft = userPlant.getLivesLeft();
        int waterLevel = userPlant.getWaterLevel();

        //생명관리
        if (livesLeft > 0) { //살아있다면
            if (gnomeEndsAt != null) {
                if (now.isBefore(gnomeEndsAt) || now.isEqual(gnomeEndsAt)) { //노움 있으면 물 계속 5
                    userPlant.setWaterLevel(5);
                    userPlant.setLastWateredAt(now);
                } else farm.setGnomeEndsAt(null);
            } else { //노움 없으면
                if (waterLevel >= 1) { //물이 하나 이상 남았을 경우
                    if (now.isAfter(lastWateredAt.plusHours(5))) { //물 준 지 5시간이 지났을 경우
                        waterLevel --;
                        userPlant.setWaterLevel(waterLevel); //물 1개 빼기
                        userPlant.setLastWateredAt(now);
                    }
                }
                if (waterLevel == 0) { //물이 0개 남았을 경우
                    if (now.isAfter(lastWateredAt.plusHours(24))) { //물이 0이 된지 24시간이 지났을 경우
                        livesLeft --; //생명 1개 빼기
                        userPlant.setLivesLeft(livesLeft);
                        userPlant.setLastWateredAt(now);
                    }
                }
            }
        }

        //성장관리
        if (livesLeft > 0) { //살아있다면
            userPlant.setGrowthProgress(0);
            if (waterLevel == 0) {
                userPlant.setGrowthProgress((float) -0.05); //물이 0이면 성장률 -5%
            }
            userPlant = applyWeather(userPlant, weatherDto); //성장률 업데이트
            float growthProgress = userPlant.getGrowthProgress();

            LocalDateTime fertilizerEndsAt = userPlant.getFertilizerEndsAt();
            if (fertilizerEndsAt != null) {
                if (now.isBefore(fertilizerEndsAt) || now.isEqual(fertilizerEndsAt)) { //영양제 있으면 + 10%
                    growthProgress += 0.1F;
                } else userPlant.setFertilizerEndsAt(null);
            }

            int corner = (farm.getCorner() != null) ? farm.getCorner().getItemId() : 0;
            if (corner == 24) growthProgress += 0.01F;
            else if (corner == 25) growthProgress += 0.03F;

            sunlightHours += sunlightHours * growthProgress; //이번에 더할 일조량
            float accumulatedSunlightHours = userPlant.getSunlightHours() + sunlightHours;

            userPlant.setGrowthProgress(growthProgress); //성장률 최종 업데이트
            userPlant.setSunlightHours(accumulatedSunlightHours); //일조량 최종 업데이트

            String difficulty = plant.getDifficulty().name();
            float max_sunlight;

            if (difficulty.equals("EASY")) max_sunlight = 20;
            else if (difficulty.equals("MEDIUM")) max_sunlight = 30;
            else max_sunlight = 40;

            if (userPlant.getGrowthStage() != UserPlant.GrowthStage.MAX) {
                if (accumulatedSunlightHours <= max_sunlight / 3) userPlant.setGrowthStage(UserPlant.GrowthStage.LEVEL1);
                else if (accumulatedSunlightHours <= max_sunlight / 3 * 2) userPlant.setGrowthStage(UserPlant.GrowthStage.LEVEL2);
                else if (accumulatedSunlightHours <= max_sunlight) userPlant.setGrowthStage(UserPlant.GrowthStage.LEVEL3);
                else {
                    userPlant.setGrowthStage(UserPlant.GrowthStage.MAX);
                    UserTitle usertitle = titleRepository.findByTitleId(user.getUserId(), plant.getPlantId());
                    if (!usertitle.isTitleCompleted()) {
                        addToPlantBook(user.getUserId(), plant.getPlantId());
                        titleService.archiveTitle(plant.getPlantId(), user.getUserId());
                    }
                }
            }
        }

        userPlantRepository.save(userPlant);

    }

    @Override
    public UserPlant applyWeather(UserPlant userPlant, WeatherDto weatherDto) {
        int death = 0;

        //낙뢰
        int lightning = Integer.parseInt(weatherDto.getLightning());
        if (lightning >= 100) death += 15;
        else if(lightning >= 50) death += 10;
        else if(lightning >= 30) death += 5;

        //풍속
        int windSpeed = Integer.parseInt(weatherDto.getWindSpeed());
        if (windSpeed >= 30) death += 15;
        else if(windSpeed >= 20) death += 10;
        else if(windSpeed >= 10) death += 5;

        int randomNumber = (int) (Math.random() * 100);

        if (randomNumber < death) { //식물 사망
            userPlant.setLivesLeft(0);
        } else { //살았다면
            float growthProgress = userPlant.getGrowthProgress();

            //1 맑, 3 구 많, 4 흐림
            //강수량 0 없, 1 비, 2 비눈, -> x 3 눈, 5 빗방울, 6 빗방울 눈날림, 7 눈날림
            int precipitation = Integer.parseInt(weatherDto.getPrecipitationType());
            if (precipitation == 1 || precipitation == 2) {
                growthProgress = 0; //비오면 성장속도 원 위치
                userPlant.setLastWateredAt(LocalDateTime.now()); //물 준 셈
            }

            if (weatherDto.getSkyStatus().equals("1")) growthProgress += 0.1F; //맑은 날 10%

            int humidity = Integer.parseInt(weatherDto.getHumidity());
            if (humidity >= 50 && humidity <= 70) growthProgress += 0.05F; //적정 습도 5%

            int temperature = Integer.parseInt(weatherDto.getTemperature());
            if (temperature >= 15 && temperature <= 25) growthProgress += 0.05F; //적정 온도 5%

            userPlant.setGrowthProgress(growthProgress);
        }

        return userPlant;
    }

    @Override
    public void addToPlantBook(int userId, int plantId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수가 없습니다"));
        Plant plant = plantRepository.findById(plantId).orElseThrow(() -> new EntityNotFoundException("해당 식물을 찾을 수 없습니다"));

        PlantBook plantbook = new PlantBook(
                0,
                user,
                plant.getPlantDescription(),
                plant.getMaxImage() == null ? "noImg" : plant.getMaxImage()
        );

        plantbookRepository.save(plantbook);
    }

}
