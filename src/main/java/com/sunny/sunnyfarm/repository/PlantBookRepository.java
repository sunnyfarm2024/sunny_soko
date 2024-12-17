package com.sunny.sunnyfarm.repository;

import com.sunny.sunnyfarm.entity.PlantBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlantBookRepository extends JpaRepository<PlantBook, Integer> {

    @Query("SELECT f FROM PlantBook f WHERE f.user.userId = :userId")
    List<PlantBook> getByUserId(Integer userId);
}
