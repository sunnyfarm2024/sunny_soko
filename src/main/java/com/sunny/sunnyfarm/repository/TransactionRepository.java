package com.sunny.sunnyfarm.repository;

import com.sunny.sunnyfarm.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
