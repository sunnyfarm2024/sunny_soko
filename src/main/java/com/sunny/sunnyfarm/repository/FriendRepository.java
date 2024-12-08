package com.sunny.sunnyfarm.repository;

import com.sunny.sunnyfarm.entity.Friend;
import com.sunny.sunnyfarm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Integer> {
    @Query("SELECT f FROM Friend f WHERE f.friendUser.userId = :userId")
    List<Friend> findByUserId(@Param("userId") int userId);

    @Query("SELECT u FROM User u WHERE u.userName LIKE %:userName%")
    List<User> findByUserName(@Param("userName") String userName);

    @Query("SELECT f FROM Friend f WHERE f.user.userId = :userId and f.friendUser.userId = :friendUserId")
    Friend findByUserIdAndFriendId(@Param("userId") int userId, @Param("friendUserId") int friendUserId);

    @Transactional
    @Modifying
    @Query("UPDATE Friend f SET f.status = :status WHERE f.user.userId = :userId and f.friendUser.userId = :friendUserId")
    void updateStatus(@Param("userId") int userId, @Param("friendUserId") int friendUserId, @Param("status") Friend.FriendStatus status);

    @Transactional
    @Modifying
    @Query("DELETE FROM Friend f WHERE f.user.userId = :userId and f.friendUser.userId = :friendUserId")
    void deleteByUserIdAndFriendUserId(@Param("userId") int userId, @Param("friendUserId") int friendUserId);
}
