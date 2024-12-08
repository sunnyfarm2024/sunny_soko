package com.sunny.sunnyfarm.service.impl;

import com.sunny.sunnyfarm.entity.Shop;
import com.sunny.sunnyfarm.entity.Transaction;
import com.sunny.sunnyfarm.entity.User;
import com.sunny.sunnyfarm.repository.TransactionRepository;
import com.sunny.sunnyfarm.service.TransactionService;
import org.springframework.stereotype.Service;


@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void recordTransaction(User user, Shop item) {
        Transaction transaction = new Transaction(
                0,
                user,
                item,
                item.getPrice(),
                Integer.parseInt(item.getItemDescription().split(" ")[0]),
                null
        );

        transactionRepository.save(transaction); // 저장
    }
}

