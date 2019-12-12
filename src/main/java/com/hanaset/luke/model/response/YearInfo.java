package com.hanaset.luke.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class YearInfo {

    private Long year;

    @JsonProperty("total_amount")
    private Long totalAmount;

    @JsonProperty("detail_amount")
    private Map<String, Long> detailAmount;
}
