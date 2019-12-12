package com.hanaset.luke.web.rest;

import com.hanaset.luke.model.request.PredictionRequest;
import com.hanaset.luke.service.LukeInfoService;
import com.hanaset.luke.service.LukePredictionService;
import com.hanaset.luke.web.rest.support.LukeApiRestSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/info")
@RestController
public class LukeInfoRest extends LukeApiRestSupport {

    private final LukeInfoService lukeInfoService;
    private final LukePredictionService lukePredictionService;

    public LukeInfoRest(LukeInfoService lukeInfoService,
                        LukePredictionService lukePredictionService) {
        this.lukeInfoService = lukeInfoService;
        this.lukePredictionService = lukePredictionService;
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

    @PostMapping("/prediction")
    public ResponseEntity predictionByBank(@RequestBody PredictionRequest request) {
        return ResponseEntity.ok(lukePredictionService.predictionBankData(request.getBank(), 2018L, request.getMonth()));
    }
}
