package com.sunny.sunnyfarm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestDto {
    private int questId;
    private String type;
    private String questDescription;
    private int reward;
    private int questProgress;
    private int questRequirement;
    private boolean isCompleted;
}
