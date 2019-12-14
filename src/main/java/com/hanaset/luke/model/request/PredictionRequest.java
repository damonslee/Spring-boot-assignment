package com.hanaset.luke.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredictionRequest {

    @NotEmpty
    private String bank;

    @NotNull
    @Max(value = 12, message = "1~12월까지 입력해주세요.")
    @Min(value = 1, message = "1~12월까지 입력해주세요.")
    private Long month;
}
