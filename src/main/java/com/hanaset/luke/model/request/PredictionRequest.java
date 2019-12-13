package com.hanaset.luke.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredictionRequest {

    @NotEmpty
    private String bank;

    @NotEmpty
    @Max(value = 12, message = "1~12월까지 입력해주세요.")
    @Min(value = 1, message = "1~12월까지 입력해주세요.")
    private Long month;
}
