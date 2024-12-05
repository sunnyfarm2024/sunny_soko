package com.sunny.sunnyfarm.service;

import com.sunny.sunnyfarm.dto.QuestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuestService {
    ResponseEntity<List<QuestDto>> getQuestList(int userId, String type);
    void updateQuestProgress(int userId, int questId);
    void claimQuestReward(int userId, int questId);
    void resetDailyQuests();
}
