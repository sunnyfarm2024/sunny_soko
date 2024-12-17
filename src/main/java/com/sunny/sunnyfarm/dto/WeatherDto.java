package com.sunny.sunnyfarm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDto {
    private String temperature;       // 기온
    private String skyStatus;         // 하늘 상태
    private String precipitationType; // 강수 형태
    private String humidity;          // 습도
    private String windSpeed;         // 풍속
    private String lightning;         // 낙뢰
}