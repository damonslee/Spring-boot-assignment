package com.hanaset.luke.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hanaset.luke.entitiy.BankEntity;
import com.hanaset.luke.entitiy.BankHistoryEntity;
import com.hanaset.luke.model.BankInfo;
import com.hanaset.luke.model.Response.SupportAmount;
import com.hanaset.luke.model.Response.YearInfo;
import com.hanaset.luke.model.Response.YearMaxInfo;
import com.hanaset.luke.repository.BankHistoryRepository;
import com.hanaset.luke.repository.BankRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LukeInfoService {

    private final BankRepository bankRepository;
    private final BankHistoryRepository bankHistoryRepository;

    public LukeInfoService(BankRepository bankRepository,
                           BankHistoryRepository bankHistoryRepository) {
        this.bankRepository = bankRepository;
        this.bankHistoryRepository = bankHistoryRepository;
    }

    private Sort sortByYear(){
        return new Sort(Sort.Direction.ASC, "year");
    }

    public List<BankInfo> getBankInfoList() {
        List<BankEntity> bankEntityList = bankRepository.findAll();
        return bankEntityList.stream().map(bankEntity ->
                BankInfo.builder()
                        .code(bankEntity.getInstituteCode())
                        .name(bankEntity.getInstituteName())
                        .build())
                .collect(Collectors.toList());
    }

    public List<YearInfo> getEveryYearInfo() {

        List<Long> years = bankHistoryRepository.getYearList();
        Map<String, String> bankInfo = bankRepository.findAll().stream().collect(Collectors.toMap(BankEntity::getInstituteCode, BankEntity::getInstituteName));
        List<YearInfo> yearInfos = Lists.newArrayList();

        for(Long year : years) {
            List<BankHistoryEntity> bankHistoryEntities = bankHistoryRepository.findByYearOrderByMonth(year);

            Long totalAmount = bankHistoryEntities.stream()
                    .map(BankHistoryEntity::getPrice).reduce(Long::sum).get();

            Map<String, Long> detailAmount = Maps.newHashMap();
            for(BankHistoryEntity bankHistoryEntity : bankHistoryEntities ) {
                detailAmount.merge(bankInfo.get(bankHistoryEntity.getBankCode()), bankHistoryEntity.getPrice(), Long::sum);
            }

            yearInfos.add(YearInfo.builder()
                    .year(year)
                    .totalAmount(totalAmount)
                    .detailAmount(detailAmount)
                    .build());
        }

        return yearInfos;
    }

    public List<YearMaxInfo> getMaximumAmountYear() {

        List<YearInfo> yearInfos = getEveryYearInfo();
        List<YearMaxInfo> yearMaxInfos = Lists.newArrayList();

        for(YearInfo yearInfo : yearInfos) {
            Map.Entry<String, Long> info = yearInfo.getDetailAmount().entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get();
            yearMaxInfos.add(YearMaxInfo.builder().year(yearInfo.getYear()).bank(info.getKey()).build());
        }

        return yearMaxInfos;
    }

    public List<SupportAmount> getExchangeBankInfo(String bank, Long start, Long end) {

        BankEntity bankEntity = bankRepository.findByInstituteName(bank);
        List<BankHistoryEntity> bankHistoryEntities = bankHistoryRepository.findByBankCodeAndYearBetween(bankEntity.getInstituteCode(), start, end);
        Map<Long ,Long> amountMap = Maps.newHashMap();

        for(Long i = start ; i <= end ; i++) {
            Long year = i;
            Long amount = bankHistoryEntities.stream().filter(bankHistoryEntity -> bankHistoryEntity.getYear().equals(year)).map(BankHistoryEntity::getPrice).reduce(Long::sum).orElse(0L);
            amountMap.put(year, Math.round(amount/12.d));
        }

        Map.Entry<Long, Long> maxEntry = amountMap.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get();
        Map.Entry<Long, Long> minEntry = amountMap.entrySet().stream().min(Comparator.comparing(Map.Entry::getValue)).get();

        List<SupportAmount> supportAmounts = Lists.newArrayList();
        supportAmounts.add(SupportAmount.builder().year(minEntry.getKey()).amount(minEntry.getValue()).build());
        supportAmounts.add(SupportAmount.builder().year(maxEntry.getKey()).amount(maxEntry.getValue()).build());

        return supportAmounts;
    }
}
