package com.sunny.sunnyfarm.service;

import com.sunny.sunnyfarm.entity.Shop;
import com.sunny.sunnyfarm.entity.Transaction;
import com.sunny.sunnyfarm.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {
    void recordTransaction(User user, Shop item);
}
