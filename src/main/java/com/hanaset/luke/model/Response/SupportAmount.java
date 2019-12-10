package com.hanaset.luke.model.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupportAmount {

    private Long year;

    private Long amount;
}
