package com.sunny.sunnyfarm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FarmDto {
    private int farmId;
    private String farmDescription;
    private UserPlantDto leftPlant;
    private UserPlantDto centerPlant;
    private UserPlantDto rightPlant;
    private int signId;
    private int cornerId;
    private LocalDateTime gnomeEndsAt;
}
