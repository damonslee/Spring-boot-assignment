package com.hanaset.luke.model.response;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class YearInfo {

    private Long year;

    @SerializedName("total_amount")
    private Long totalAmount;

    @SerializedName("detail_amount")
    private Map<String, Long> detailAmount;
}
