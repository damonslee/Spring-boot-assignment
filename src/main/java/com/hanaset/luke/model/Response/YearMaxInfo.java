package com.hanaset.luke.model.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class YearMaxInfo {

    private Long year;

    private String bank;
}
