package com.sunny.sunnyfarm.impl;

import com.sunny.sunnyfarm.dto.QuestDto;
import com.sunny.sunnyfarm.dto.ShopDto;
import com.sunny.sunnyfarm.entity.Quest;
import com.sunny.sunnyfarm.entity.Shop;
import com.sunny.sunnyfarm.repository.QuestRepository;
import com.sunny.sunnyfarm.repository.ShopRepository;
import com.sunny.sunnyfarm.service.ShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopServiceImpl implements ShopService {

    private final ShopRepository shopRepository;

    public ShopServiceImpl(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Override
    public ResponseEntity<List<ShopDto>> getItemList() {
        List<Shop> shop = shopRepository.getItemList();

        List<ShopDto> itemList = new ArrayList<>();
        for (Shop item : shop) {
            ShopDto shopDto = new ShopDto(
                    item.getItemName(),
                    item.getItemDescription(),
                    item.getPrice(),
                    item.getCategory().name()
            );
            itemList.add(shopDto);
        }
        return ResponseEntity.ok(itemList);
    }

    @Override
    public ResponseEntity<String> checkItemAvailability(int userId, int itemId) {
        Shop.ItemCategory category = Shop.ItemCategory.DECORATION;
        boolean isAvailable = shopRepository.findByCategory(userId, category);
        return null;
    }

    @Override
    public ResponseEntity<String> purchaseItem(int userId, int itemId) {
        return null;
    }


}
