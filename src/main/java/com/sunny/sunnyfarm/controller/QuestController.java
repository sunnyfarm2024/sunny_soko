package com.sunny.sunnyfarm.controller;

import com.sunny.sunnyfarm.dto.QuestDto;
import com.sunny.sunnyfarm.service.QuestService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/quest")
public class QuestController {
    private final QuestService questService;

    public QuestController(QuestService questService) {this.questService = questService;}

    @GetMapping("/list")
    ResponseEntity<List<QuestDto>> getQuestList(HttpSession session, @RequestParam String type) {
        session.setAttribute("userId", 1);
        Integer userId = (Integer) session.getAttribute("userId");
        return questService.getQuestList(userId, type);
    }

    @PostMapping("/progress")
    ResponseEntity<String> updateQuestProgress(HttpSession session, @RequestParam int questId) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) userId = 1;
        try {
            questService.updateQuestProgress(userId, questId); // Service 메서드 호출
            return ResponseEntity.ok("퀘스트 진행 성공"); // 성공 시 메시지 반환
        } catch (Exception e) {
            e.printStackTrace(); // 에러 로그 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("퀘스트 진행 실패"); // 실패 시 메시지 반환
        }
    }

    @PostMapping("/reward")
    ResponseEntity<String> claimQuestReward(HttpSession session, @RequestParam int questId) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) userId = 1;
        try {
            questService.claimQuestReward(userId, questId); // Service 메서드 호출
            return ResponseEntity.ok("퀘스트 보상 수령 성공"); // 성공 시 메시지 반환
        } catch (Exception e) {
            e.printStackTrace(); // 에러 로그 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("퀘스트 보상 수령 실패"); // 실패 시 메시지 반환
        }
    }

    @PostMapping("/reset")
    ResponseEntity<String> resetDailyQuests() {
        try {
            questService.resetDailyQuests(); // Service 메서드 호출
            return ResponseEntity.ok("일일 퀘스트 리셋 성공"); // 성공 시 메시지 반환
        } catch (Exception e) {
            e.printStackTrace(); // 에러 로그 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("일일 퀘스트 리셋 실패"); // 실패 시 메시지 반환
        }
    }

}
