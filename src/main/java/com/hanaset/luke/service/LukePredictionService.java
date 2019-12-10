package com.hanaset.luke.service;

import com.hanaset.luke.repository.BankHistoryRepository;
import com.hanaset.luke.repository.BankRepository;
import org.springframework.stereotype.Service;

@Service
public class LukePredictionService {

    private final BankRepository bankRepository;
    private final BankHistoryRepository bankHistoryRepository;

    public LukePredictionService(BankRepository bankRepository,
                                 BankHistoryRepository bankHistoryRepository) {
        this.bankHistoryRepository = bankHistoryRepository;
        this.bankRepository = bankRepository;
    }
}
