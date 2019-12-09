package com.hanaset.luke.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankInfo {

    private String code;

    private String name;
}
