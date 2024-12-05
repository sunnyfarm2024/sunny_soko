package com.sunny.sunnyfarm.controller;

import com.sunny.sunnyfarm.dto.QuestDto;
import com.sunny.sunnyfarm.dto.ShopDto;
import com.sunny.sunnyfarm.service.QuestService;
import com.sunny.sunnyfarm.service.ShopService;
import com.sunny.sunnyfarm.service.TitleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/shop")
public class ShopController {
    private final ShopService shopService;

    public ShopController(ShopService shopService) {this.shopService = shopService;}


    @GetMapping("/list")
    ResponseEntity<List<ShopDto>> getItemList(HttpSession session) {
        ResponseEntity<List<ShopDto>> shopItemsResponse = shopService.getItemList();
        List<ShopDto> shopItems = shopItemsResponse.getBody();
        if (shopItems != null) {
            session.setAttribute("shopItems", shopItems);
        }
        return shopItemsResponse;
    }

    @GetMapping("/decoration_check")
    ResponseEntity<String> checkItemAvailability(@RequestParam int userId, @RequestParam int itemId) {
        return shopService.checkItemAvailability(userId, itemId);
    }


}
