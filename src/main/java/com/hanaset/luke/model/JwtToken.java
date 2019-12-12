package com.hanaset.luke.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class JwtToken {

    private String token;

    @JsonProperty("expired_time")
    private ZonedDateTime expiredTime;

    @JsonProperty("expired_string")
    private String expiredString;
}
