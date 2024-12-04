package com.sunny.sunnyfarm.controller;

import com.sunny.sunnyfarm.dto.QuestDto;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class QuestController {
    List<QuestDto> getQuestList(@RequestParam int userId, @RequestParam String type) {
        return null;
    }
}
