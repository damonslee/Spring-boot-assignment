package com.hanaset.luke.service;

import com.google.common.collect.Lists;
import com.hanaset.luke.entity.BankHistoryEntity;
import com.hanaset.luke.model.PredictionInfo;
import com.hanaset.luke.model.response.PredictionResponse;
import com.hanaset.luke.repository.BankHistoryRepository;
import com.hanaset.luke.repository.BankRepository;
import com.hanaset.luke.web.rest.exception.ErrorCode;
import com.hanaset.luke.web.rest.exception.LukeApiRestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LukePredictionService {

    private final BankRepository bankRepository;
    private final BankHistoryRepository bankHistoryRepository;

    public LukePredictionService(BankRepository bankRepository,
                                 BankHistoryRepository bankHistoryRepository) {
        this.bankHistoryRepository = bankHistoryRepository;
        this.bankRepository = bankRepository;
    }

    public PredictionResponse predictionBankData(String bankName, Long year, Long month) {

        String bankCode = bankRepository.findByInstituteName(bankName)
                .orElseThrow(() -> new LukeApiRestException(ErrorCode.BANK_NOT_FOUND, "입력하신 은행이 존재하지 않습니다."))
                .getInstituteCode();
        List<BankHistoryEntity> entityList = bankHistoryRepository.findByBankCodeOrderByYearAscMonthAsc(bankCode);

        List<PredictionInfo> predictionInfos = Lists.newArrayList();
        long avgInc = 0L;
        for (int i = 1; i < entityList.size(); i++) {

            long gap = entityList.get(i).getPrice() - entityList.get(i - 1).getPrice();
            avgInc += gap;
        }

        avgInc /= entityList.size() - 1; // 2005/1 ~ 2017/10 까지의 평균 증액

        Long totalData = entityList.get(0).getPrice();
        for (int i = 1; i < entityList.size(); i++) {
            totalData += avgInc;
            predictionInfos.add(
                    PredictionInfo.builder()
                            .year(entityList.get(i).getYear())
                            .month(entityList.get(i).getMonth())
                            .gap(entityList.get(i).getPrice() - totalData)
                            .gapPercent(entityList.get(i).getPrice() / Double.valueOf(totalData))
                            .price(entityList.get(i).getPrice())
                            .build()
            );
        }

        List<PredictionInfo> monthPredicationInfos = predictionInfos.stream().filter(predictionInfo -> predictionInfo.getMonth().equals(month)).collect(Collectors.toList());

        double weight = 0.077; // 임의의 가중치
        double avgMouthGapPercent = 0D;
        for (int i = 0; i < monthPredicationInfos.size(); i++) {
            avgMouthGapPercent += monthPredicationInfos.get(i).getGapPercent() * (1 + (weight * i));
        }

        avgMouthGapPercent /= monthPredicationInfos.size();

        long period = (12 - (entityList.get(0).getMonth())) + ((((year - 1) - entityList.get(0).getYear())) * 12) + month;

        long data = entityList.get(0).getPrice() + (avgInc * period);
        long result = Math.round(data * avgMouthGapPercent);
        return PredictionResponse.builder()
                .bank(bankCode)
                .year(year)
                .month(month)
                .amount(result)
                .build();

    }
}
