package com.hanaset.luke.model;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class JwtToken {

    private String token;

    private ZonedDateTime expireTime;
}
