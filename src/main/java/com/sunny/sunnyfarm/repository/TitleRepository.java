package com.sunny.sunnyfarm.repository;

import com.sunny.sunnyfarm.entity.UserTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TitleRepository extends JpaRepository<UserTitle, Integer> {


    @Query("SELECT u FROM UserTitle u JOIN FETCH u.title WHERE u.user.userId = :userId AND u.isTitleCompleted = TRUE")
    List<UserTitle> findByUserId(@Param("userId") int userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user SET selected_title_id = :titleId WHERE user_id = :userId", nativeQuery = true)
    int updateSelectedTitle(@Param("titleId") int titleId, @Param("userId") int userId);

    @Modifying
    @Transactional
    @Query("UPDATE UserTitle ut SET ut.isActive = CASE WHEN ut.title.titleId = :titleId THEN true ELSE false END WHERE ut.user.userId = :userId")
    int updateIsActive(@Param("titleId") int titleId, @Param("userId") int userId);

    @Modifying
    @Transactional
    @Query("UPDATE UserTitle ut SET ut.titleProgress = ut.titleProgress + 1, ut.isTitleCompleted = true WHERE ut.user.userId = :userId AND ut.title.titleId = :plantId")
    void updateMasterTitle(@Param("userId") int userId, @Param("plantId") int plantId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE usertitle ut " +
            "SET ut.title_progress = ut.title_progress + 1, " +
            "    ut.is_title_completed = CASE " +
            "        WHEN ut.title_progress >= (SELECT t.title_requirement FROM title t WHERE t.title_id = ut.title_id) " +
            "        THEN TRUE " +
            "        ELSE ut.is_title_completed " +
            "    END " +
            "WHERE ut.user_id = :userId AND ut.title_id = :titleId", nativeQuery = true)
    void updateFarmerTitle(@Param("userId") int userId, @Param("titleId") int titleId);

    @Query("SELECT ut FROM UserTitle ut JOIN FETCH ut.title WHERE ut.user.userId = :userId AND ut.title.titleId = :plantId")
    UserTitle findByTitleId(@Param("userId") int userId, @Param("plantId") int plantId);
}
