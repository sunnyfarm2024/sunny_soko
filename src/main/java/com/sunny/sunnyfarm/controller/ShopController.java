package com.sunny.sunnyfarm.controller;

import com.sunny.sunnyfarm.dto.ShopDto;
import com.sunny.sunnyfarm.entity.Shop;
import com.sunny.sunnyfarm.service.ShopService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/shop")
public class ShopController {
    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("/list")
    ResponseEntity<List<ShopDto>> getItemList(HttpSession session) {
        List<Shop> shop = shopService.getItemList();
        session.setAttribute("shop", shop);

        List<ShopDto> itemList = shop.stream()
                .map(item -> new ShopDto(
                        item.getItemName(),
                        item.getItemDescription(),
                        item.getPrice(),
                        item.getCategory().name()
                ))
                .toList();

        return ResponseEntity.ok(itemList);
    }

    @GetMapping("/check")
    ResponseEntity<Object> checkItemAvailability(HttpSession session, @RequestParam String itemName) {
        Integer userId = (Integer) session.getAttribute("userId");
        @SuppressWarnings("unchecked")
        List<Shop> shop = (List<Shop>) session.getAttribute("shop");

        if (shop == null) {
            return ResponseEntity.status(404).body("시스템 오류: 아이템 리스트가 없습니다.");
        }

        Shop item = shop.stream()
                .filter(itemToPurchase -> itemToPurchase.getItemName().equals(itemName))
                .findFirst()
                .orElse(null);


        if (item == null) {
            return ResponseEntity.status(404).body("아이템을 찾을 수 없습니다.");
        }

        if (item.getCurrency() == Shop.CurrencyType.CASH) {
            Map<String, Object> response = new HashMap<>();
            response.put("check", "cash");
            response.put("message", "CASH 결제는 결제 링크를 통해 진행해야 합니다.");
            response.put("itemName", item.getItemName());
            response.put("price", item.getPrice());
            return ResponseEntity.status(400).body(response);
        }

        //session farm cornerId != itemId <-  넣어야함
        boolean exist = shopService.checkItemAvailability(userId, item.getItemId());

        if (exist) {
            Map<String, Object> response = new HashMap<>();
            response.put("check", "decoration");
            response.put("message", "중복된 인테리어입니다.");
            return ResponseEntity.status(400).body(response);
        } else {
            return ResponseEntity.status(200).body("구매 가능한 아이템입니다");
        }
    }

    @PostMapping("/purchase")
    ResponseEntity<Object> purchaseItem(@RequestParam String itemName, HttpSession session) {

        // 확인용 출력
        System.out.println("먀아아아아아악Item Name: " + itemName);


        Integer userId = (Integer) session.getAttribute("userId");
        @SuppressWarnings("unchecked")
        List<Shop> shop = (List<Shop>) session.getAttribute("shop");

        Shop item = shop.stream()
                .filter(itemToPurchase -> itemToPurchase.getItemName().equals(itemName))
                .findFirst()
                .orElse(null);


        return shopService.purchaseItem(userId, item);

    }
}
