package com.hanaset.luke.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hanaset.luke.entitiy.BankEntity;
import com.hanaset.luke.entitiy.BankHistoryEntity;
import com.hanaset.luke.model.BankInfo;
import com.hanaset.luke.model.Response.YearInfo;
import com.hanaset.luke.repository.BankHistoryRepository;
import com.hanaset.luke.repository.BankRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
        List<YearInfo> yearInfos = Lists.newArrayList();

        for(Long year : years) {
            List<BankHistoryEntity> bankHistoryEntities = bankHistoryRepository.findByYearOrderByMonth(year);

            Long totalAmount = bankHistoryEntities.stream()
                    .map(BankHistoryEntity::getPrice).reduce(Long::sum).get();

            Map<String, Long> detailAmount = Maps.newHashMap();
            for(BankHistoryEntity bankHistoryEntity : bankHistoryEntities ) {
                detailAmount.merge(bankHistoryEntity.getBankCode().getInstituteName(), bankHistoryEntity.getPrice(), Long::sum);
            }

            yearInfos.add(YearInfo.builder()
                    .year(year)
                    .totalAmount(totalAmount)
                    .detailAmount(detailAmount)
                    .build());
        }

        return yearInfos;
    }
}
