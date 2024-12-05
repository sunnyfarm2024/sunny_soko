package com.sunny.sunnyfarm.impl;

import com.sunny.sunnyfarm.dto.QuestDto;
import com.sunny.sunnyfarm.entity.*;
import com.sunny.sunnyfarm.repository.QuestRepository;
import com.sunny.sunnyfarm.service.QuestService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestServiceImpl implements QuestService {
    private final QuestRepository questRepository;

    public QuestServiceImpl(QuestRepository questRepository) {
        this.questRepository = questRepository;
    }

    @Override
    public ResponseEntity<List<QuestDto>> getQuestList(int userId, String type) {
        System.out.println("userId: " + userId + ", type: " + type);
        Quest.QuestType questType;
        try {
            questType = Quest.QuestType.valueOf(type.toUpperCase()); // Ensure enum compatibility
        } catch (IllegalArgumentException e) {
            return null;
        }

        List<UserQuest> userQuests = questRepository.findUserQuests(userId, questType);

        List<QuestDto> questDtoList = userQuests.stream()
                .map(userQuest -> {
                    Quest quest = userQuest.getQuest();
                    return new QuestDto(
                            quest.getType().name(),
                            quest.getQuestDescription(),
                            quest.getReward(),
                            userQuest.getQuestProgress(),
                            quest.getQuestRequirement(),
                            userQuest.isQuestCompleted()
                    );
                }).collect(Collectors.toList());
        return ResponseEntity.ok(questDtoList);
    }

    @Override
    public void updateQuestProgress(int userId, int questId) {
        UserQuest userquest = questRepository.findUserQuestByQuestId(userId, questId);

        int progress = userquest.getQuestProgress(); //4
        if (progress < userquest.getQuest().getQuestRequirement()) {
            progress += 1;
            userquest.setQuestProgress(progress);
        }

        questRepository.save(userquest);
        System.out.println("UserQuest updated successfully.");
    }


    @Override
    public void claimQuestReward(int userId, int questId) {
        UserQuest userquest = questRepository.findUserQuestByQuestId(userId, questId);

        if (!userquest.isQuestCompleted() && userquest.getQuestProgress() == userquest.getQuest().getQuestRequirement()) {
            if (userquest.getQuest().getType() == Quest.QuestType.DAILY) {
                userquest.setQuestCompleted(true);
            } else {
                userquest.setQuestProgress(0);
            }
            questRepository.save(userquest);
            System.out.println("UserQuest updated successfully.");
        }
    }

    @Override
    public void resetDailyQuests() {
        questRepository.resetDailyQuests();
    }
}
