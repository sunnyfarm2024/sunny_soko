package com.sunny.sunnyfarm.repository;

import com.sunny.sunnyfarm.entity.Quest;
import com.sunny.sunnyfarm.entity.Shop;
import com.sunny.sunnyfarm.entity.UserQuest;
import com.sunny.sunnyfarm.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Integer> {

    @Query("SELECT s FROM Shop s")
    List<Shop> getItemList();

    @Query("SELECT i FROM Inventory i WHERE i.item.category = :category and i.user.userId = :userId")
    boolean findByCategory(@Param("userId") int userId, @Param("category") Shop.ItemCategory category);

    /*@Modifying
    @Transactional
    @Query("UPDATE UserQuest uq SET uq.isQuestCompleted = FALSE, uq.questProgress = 0 where uq.quest.type = 'DAILY'")
    void resetDailyQuests();*/
}
