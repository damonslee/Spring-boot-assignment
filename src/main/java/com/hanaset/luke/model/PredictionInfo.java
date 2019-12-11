package com.hanaset.luke.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PredictionInfo {

    private Long year;

    private Long month;

    private Long gap;

    private Double gapPercent;

    private Long price;
}
