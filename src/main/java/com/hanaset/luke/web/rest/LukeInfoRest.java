package com.hanaset.luke.web.rest;

import com.hanaset.luke.service.LukeInfoService;
import com.hanaset.luke.web.rest.support.LukeApiRestSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/info")
@RestController
public class LukeInfoRest extends LukeApiRestSupport {

    private final LukeInfoService lukeInfoService;

    public LukeInfoRest(LukeInfoService lukeInfoService) {
        this.lukeInfoService = lukeInfoService;
    }

    @GetMapping("/bank_list")
    public ResponseEntity getBankList() {
        return success(lukeInfoService.getBankInfoList());
    }

    @GetMapping("/every_year")
    public ResponseEntity getEveryYearInfo() {
        return response(lukeInfoService.getEveryYearInfo(), "name", "주택금융 공급현황");
    }

    @GetMapping("/maximum_amount_year")
    public ResponseEntity getMaximumAmountYear() {
        return ResponseEntity.ok(lukeInfoService.getMaximumAmountYear());
    }

    @GetMapping("/history_max_min")
    public ResponseEntity getMaxMinInfo() {
        String bank = "외환은행";
        return response(lukeInfoService.getExchangeBankInfo(bank, 2005L, 2016L), "bank", bank);
    }
}
