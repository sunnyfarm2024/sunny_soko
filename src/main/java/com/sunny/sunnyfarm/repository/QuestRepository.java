package com.sunny.sunnyfarm.repository;

import com.sunny.sunnyfarm.entity.Quest;
import com.sunny.sunnyfarm.entity.UserQuest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QuestRepository extends JpaRepository<UserQuest, Integer> {
    @Query("SELECT uq FROM UserQuest uq JOIN FETCH uq.quest WHERE uq.user.userId = :userId and uq.quest.type = :type")
    List<UserQuest> findUserQuests(@Param("userId") int userId, @Param("type") Quest.QuestType type);

    @Query("SELECT uq FROM UserQuest uq JOIN FETCH uq.quest WHERE uq.user.userId = :userId And uq.quest.questId = :questId")
    UserQuest findUserQuestByQuestId(@Param("userId") int userId, @Param("questId") int questId);

    @Modifying
    @Transactional
    @Query("UPDATE UserQuest uq SET uq.isQuestCompleted = FALSE, uq.questProgress = 0 where uq.quest.type = 'DAILY'")
    void resetDailyQuests();

    @Modifying
    @Transactional
    @Query("UPDATE UserQuest uq SET uq.questProgress = 1 where uq.quest.questId = 8")
    void resetAttendQuests();
}
