package com.hanaset.luke.service;

import com.google.common.collect.Lists;
import com.hanaset.luke.entity.BankEntity;
import com.hanaset.luke.entity.BankHistoryEntity;
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
@SuppressWarnings("Duplicates")
public class LukeFileService {

    private final BankHistoryRepository bankHistoryRepository;
    private final BankRepository bankRepository;

    public LukeFileService(BankHistoryRepository bankHistoryRepository,
                           BankRepository bankRepository) {
        this.bankHistoryRepository = bankHistoryRepository;
        this.bankRepository = bankRepository;
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

        List<BankHistoryEntity> bankHistoryEntityList = Lists.newArrayList();
        String fileName = file.getOriginalFilename();

        try {
            Reader reader = new InputStreamReader(file.getInputStream());  // IOException
            CSVReader csvReader = new CSVReaderBuilder(reader).build();  // CsvException
            List<String[]> list = csvReader.readAll();

//            for (String[] row : list) {
//
//                for (int i = 2; i < row.length; i++) {
//
//                    if (row[i].equals(""))
//                        break;
//
//                    bankHistoryEntityList.add(BankHistoryEntity.builder()
//                            .year(Long.parseLong(row[0]))
//                            .month(Long.parseLong(row[1]))
//                            .bankCode(bankInfos.get(i - 2).getCode())
//                            .price(Long.parseLong(row[i].replaceAll("[^0-9]", "")))
//                            .build());
//                }
//            }

            List<BankInfo> bankInfos = uploadAndGetBankInfo(list.get(0));

            for (int i = 1 ; i < list.size() ; i++){

                for (int j = 2; j < list.get(i).length; j++) {

                    if (list.get(i)[j].equals(""))
                        break;

                    bankHistoryEntityList.add(BankHistoryEntity.builder()
                            .year(Long.parseLong(list.get(i)[0]))
                            .month(Long.parseLong(list.get(i)[1]))
                            .bankCode(bankInfos.get(j - 2).getCode())
                            .price(Long.parseLong(list.get(i)[j].replaceAll("[^0-9]", "")))
                            .build());
                }
            }

            bankHistoryRepository.saveAll(bankHistoryEntityList);

        } catch (NumberFormatException e) {
            throw new LukeApiRestException(ErrorCode.FILE_UPLOAD_FAIL, e.getMessage());
        } catch (CsvException e) {
            throw new LukeApiRestException(ErrorCode.FILE_UPLOAD_FAIL, e.getMessage());
        } catch (IOException e) {
            throw new LukeApiRestException(ErrorCode.FILE_UPLOAD_FAIL, e.getMessage());
        }

        return fileName + " 을 정상적으로 업로드 하였습니다.";
    }

    public List<BankInfo> uploadAndGetBankInfo(String[] banks) {

        Map<String, String> bankInfoMap = getBankInfoList().stream().collect(Collectors.toMap(BankInfo::getName, BankInfo::getCode));
        List<BankEntity> bankEntities = Lists.newArrayList();
        List<BankInfo> bankInfos = Lists.newArrayList();
        int count = 1;

        for(int i = 0 ; i < banks.length ; i++ ) {

            String bankName = getBankName(banks[i]);
            if(bankName.equals("")) {
                continue;
            }

            if(bankInfoMap.containsKey(bankName)) {
                bankInfos.add(BankInfo.builder()
                        .name(bankName)
                        .code(bankInfoMap.get(bankName))
                        .build());

            } else {
                String code = "bnk" + (count + bankInfoMap.size());
                bankEntities.add(BankEntity.builder()
                        .instituteName(bankName)
                        .instituteCode(code)
                        .build());
                count++;
                bankInfos.add(BankInfo.builder()
                        .code(code)
                        .name(bankName)
                        .build());
            }
        }

        bankRepository.saveAll(bankEntities);
        return bankInfos;
    }

    public String getBankName(String name) {
        String result = name.replaceAll("[0-9() | (연도) | 월 | (억원)]","");
        return  result;
    }

}
