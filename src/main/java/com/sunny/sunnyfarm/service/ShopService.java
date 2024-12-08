package com.sunny.sunnyfarm.service;

import com.sunny.sunnyfarm.entity.Shop;
import org.springframework.http.ResponseEntity;

import java.util.List;
public interface ShopService {
    List<Shop> getItemList();
    boolean checkItemAvailability(int userId, int itemId);
    ResponseEntity<Object> purchaseItem(int userId, Shop item);
}
