package com.sunny.sunnyfarm.service;

import com.sunny.sunnyfarm.dto.FriendDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface FriendService {
    Map<String, List<FriendDto>> getFriendList(int userId);
    List<FriendDto> searchFriend(String userName);
    ResponseEntity<String> sendFriendRequest(int userId, int friendUserId);
    ResponseEntity<String> acceptFriendRequest(int userId, int friendUserId);
    ResponseEntity<String> rejectFriendRequest(int userId, int friendUserId);
    ResponseEntity<String> waterFriendPlant(int userId, int friendUserId, int plantId);
}
