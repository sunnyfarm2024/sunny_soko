package com.sunny.sunnyfarm.controller;

import com.sunny.sunnyfarm.dto.TitleDto;
import com.sunny.sunnyfarm.service.TitleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/title")
public class TitleController {

    private final TitleService titleService;

    public TitleController(TitleService titleService) {
        this.titleService = titleService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<TitleDto>> getTitleList(@RequestParam int userId) {
        return titleService.getTitleList(userId);
    }

    @PutMapping("/change")
    public ResponseEntity<String> changeTitle(@RequestParam int userId, @RequestParam int titleId) {
        boolean result = titleService.changeTitle(titleId, userId);

        if (result) {
            return ResponseEntity.ok("칭호 변경 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("칭호 변경 실패");
        }
    }

    @PutMapping("/progress")
    public ResponseEntity<String> changeProgress(@RequestParam int userId, @RequestParam int plantId) {
        boolean result = titleService.archiveTitle(plantId, userId);

        if (result) {
            return ResponseEntity.ok("칭호 진행 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("칭호 진행 실패");
        }
    }

}
