package com.sunny.sunnyfarm.controller;

import com.sunny.sunnyfarm.dto.GuestbookDto;
import com.sunny.sunnyfarm.dto.ShopDto;
import com.sunny.sunnyfarm.entity.GuestBook;
import com.sunny.sunnyfarm.entity.Shop;
import com.sunny.sunnyfarm.service.GuestbookService;
import com.sunny.sunnyfarm.service.ShopService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/guestbook")
public class GuestbookController {
    private final GuestbookService guestbookService;

    public GuestbookController(GuestbookService guestbookService) {
        this.guestbookService = guestbookService;
    }

    @GetMapping("/list")
    ResponseEntity<List<GuestbookDto>> getGuestbook (HttpSession session, int userId) {
        List<GuestbookDto> guestbookList = guestbookService.getGuestbook(userId);
        return ResponseEntity.ok(guestbookList);
    }

    @PostMapping("/write")
    ResponseEntity<String> writeGuestbook(HttpSession session, int friendUserId, String content) {
        Integer userId = (Integer) session.getAttribute("userId");
        try {
            guestbookService.writeGuestbook(userId, friendUserId, content);
            return ResponseEntity.ok("방명록을 작성했습니다.");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(404).body("방명록 작성에 실패했습니다.");
        }
    }

    @PostMapping("/check")
    ResponseEntity<String> checkGuestbook(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");

        try {
            guestbookService.checkRead(userId);
            return ResponseEntity.ok("방명록을 읽었습니다.");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(404).body("방명록 읽기에 실패했습니다.");
        }
    }
}
