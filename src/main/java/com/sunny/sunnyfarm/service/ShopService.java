package com.sunny.sunnyfarm.service;

import com.sunny.sunnyfarm.dto.ShopDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
public interface ShopService {
    ResponseEntity<List<ShopDto>> getItemList();
    ResponseEntity<String> checkItemAvailability(int userId, int itemId);
    ResponseEntity<String> purchaseItem(int userId, int itemId);
}
