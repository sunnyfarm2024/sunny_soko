package com.sunny.sunnyfarm.controller;

import com.sunny.sunnyfarm.dto.FriendDto;
import com.sunny.sunnyfarm.service.FriendService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/friend")
public class FriendController {
    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, List<FriendDto>>> getFriendList(HttpSession session) {
        session.setAttribute("userId", 1);
        Integer userId = (Integer) session.getAttribute("userId");

        Map<String, List<FriendDto>> friendListByStatus = friendService.getFriendList(userId);
        return ResponseEntity.ok(friendListByStatus);
    }

    @GetMapping("/search")
    public ResponseEntity<List<FriendDto>> searchFriend(@RequestParam String userName) {
        try {
            List<FriendDto> searchList = friendService.searchFriend(userName);
            return ResponseEntity.ok(searchList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/pending")
    public ResponseEntity<String> sendFriendRequest(HttpSession session, @RequestParam int friendUserId) {
        session.setAttribute("userId", 1);
        Integer userId = (Integer) session.getAttribute("userId");
        if (friendUserId == userId) {return ResponseEntity.status(400).body("본인입니다.");}
        return friendService.sendFriendRequest(userId, friendUserId);
    }


    @PostMapping("/accept")
    public ResponseEntity<String> acceptFriendRequest(HttpSession session, @RequestParam int friendUserId) {
        session.setAttribute("userId", 1);
        Integer userId = (Integer) session.getAttribute("userId");
        return friendService.acceptFriendRequest(userId, friendUserId);
    }

    @PostMapping("/reject")
    ResponseEntity<String> rejectFriendRequest(HttpSession session, @RequestParam int friendUserId) {
        session.setAttribute("userId", 1);
        Integer userId = (Integer) session.getAttribute("userId");
        return friendService.rejectFriendRequest(userId, friendUserId);
    }

    @PostMapping("/water")
    ResponseEntity<String> waterFriendPlant(HttpSession session, @RequestParam int friendUserId, @RequestParam int plantId) {
        session.setAttribute("userId", 1);
        Integer userId = (Integer) session.getAttribute("userId");
        return friendService.waterFriendPlant(userId, friendUserId, plantId);
    }
}
