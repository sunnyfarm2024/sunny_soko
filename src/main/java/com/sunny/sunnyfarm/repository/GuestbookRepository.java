package com.sunny.sunnyfarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sunny.sunnyfarm.entity.GuestBook;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GuestbookRepository extends JpaRepository<GuestBook, Integer> {

    @Query("SELECT g FROM GuestBook g WHERE g.user.userId = :userId")
    List<GuestBook> findByUserId(int userId);

    @Transactional
    @Modifying
    @Query("UPDATE GuestBook g SET g.isRead = TRUE WHERE g.user.userId = :userId AND g.isRead = FALSE")
    void updateIsReadByUserId(@Param("userId") int userId);
}
