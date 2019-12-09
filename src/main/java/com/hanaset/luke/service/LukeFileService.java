package com.hanaset.luke.service;

import com.google.common.collect.Lists;
import com.hanaset.luke.entitiy.BankEntity;
import com.hanaset.luke.entitiy.BankHistoryEntity;
import com.hanaset.luke.model.BankInfo;
import com.hanaset.luke.repository.BankHistoryRepository;
import com.hanaset.luke.repository.BankRepository;
import com.hanaset.luke.web.rest.exception.ErrorCode;
import com.hanaset.luke.web.rest.exception.LukeApiRestException;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LukeFileService {

    private final BankHistoryRepository bankHistoryRepository;
    private final BankRepository bankRepository;
    private Map<String, String> bankMap = null;

    public LukeFileService(BankHistoryRepository bankHistoryRepository,
                           BankRepository bankRepository) {
        this.bankHistoryRepository = bankHistoryRepository;
        this.bankRepository = bankRepository;
    }

    private void initBankCode() {
        List<BankEntity> bankEntityList = bankRepository.findAll();
        bankMap = bankEntityList.stream().collect(Collectors.toMap(BankEntity::getInstituteCode, BankEntity::getInstituteName));
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

    public String uploadFileData(MultipartFile file) {

        List<BankInfo> bankInfos = getBankInfoList();
        List<BankHistoryEntity> bankHistoryEntityList = Lists.newArrayList();
        String fileName = file.getOriginalFilename();

        try {
            Reader reader = new InputStreamReader(file.getInputStream());
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            List<String[]> list = csvReader.readAll();

            for(String[] row : list) {

                for(int i = 2 ; i < row.length ; i++) {

                    if(row[i].equals(""))
                        break;

                    bankHistoryEntityList.add(BankHistoryEntity.builder()
                            .year(Long.parseLong(row[0]))
                            .month(Long.parseLong(row[1]))
                            .bankCode(BankEntity.builder().instituteCode(bankInfos.get(i-2).getCode()).build())
                            .price(Long.parseLong(row[i].replaceAll("[^0-9]", "")))
                            .build());
                }
            }

            bankHistoryRepository.saveAll(bankHistoryEntityList);

        }catch (IOException e) {
            throw new LukeApiRestException(ErrorCode.FILE_UPLOAD_FAIL, e.getMessage());
        }catch (CsvException e) {
            throw new LukeApiRestException(ErrorCode.FILE_UPLOAD_FAIL, e.getMessage());
        }catch (NumberFormatException e) {
            throw new LukeApiRestException(ErrorCode.FILE_UPLOAD_FAIL, e.getMessage());
        }

        return fileName + " 을 정상적으로 업로드 하였습니다.";
    }

}
