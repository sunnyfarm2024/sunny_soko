package com.sunny.sunnyfarm.service.impl;

import com.sunny.sunnyfarm.entity.Shop;
import com.sunny.sunnyfarm.entity.User;
import com.sunny.sunnyfarm.repository.ShopRepository;
import com.sunny.sunnyfarm.repository.UserRepository;
import com.sunny.sunnyfarm.service.ShopService;
import com.sunny.sunnyfarm.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {

    private final TransactionService transactionService;

    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    public ShopServiceImpl(TransactionService transactionService, ShopRepository shopRepository, UserRepository userRepository) {
        this.transactionService = transactionService;
        this.shopRepository = shopRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Shop> getItemList() {
        List<Shop> shop = shopRepository.getItemList();

        return shop;
    }

    @Override
    public boolean checkItemAvailability(int userId, int itemId) {
        Shop.ItemCategory category = shopRepository.findByCategory(userId, itemId);

        return category == Shop.ItemCategory.DECORATION;
    }

    @Override
    public ResponseEntity<Object> purchaseItem(int userId, Shop item) {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다"));

        Shop.CurrencyType currency = item.getCurrency();
        int price = item.getPrice();

        //재화 처리
        if (currency == Shop.CurrencyType.COIN) {
            int coin = user.getCoinBalance();
            if (coin >= price) user.setCoinBalance(coin - price);
            else return ResponseEntity.status(400).body("코인이 부족합니다.");
        } else if (currency == Shop.CurrencyType.DIAMOND) {
            int diamond = user.getDiamondBalance();
            if (diamond >= price) user.setDiamondBalance(diamond - price);
            else return ResponseEntity.status(400).body("다이아가 부족합니다.");
        } else {
            transactionService.recordTransaction(user, item);
        }

        if (item.getCategory() == Shop.ItemCategory.CURRENCY) {
            try {
                price = Integer.parseInt(item.getItemDescription().split(" ")[0]);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(400).body("아이템 설명에서 가격을 읽을 수 없습니다.");
            }

            if (item.getCurrency() == Shop.CurrencyType.CASH) {
                user.setDiamondBalance(user.getDiamondBalance() + price);
            } else {
                user.setCoinBalance(user.getCoinBalance() + price);
            }
        } else {
            /*if (!inventoryService.checkAvailableSlot(userId)) {
                return ResponseEntity.status(400).body("인벤토리가 꽉 찼습니다.");
            }
            inventoryService.addItem(userId, item.getItemId());*/
        }

        userRepository.save(user); // 재화 업데이트 저장
        return ResponseEntity.status(200).body("구매 성공");
    }


}
