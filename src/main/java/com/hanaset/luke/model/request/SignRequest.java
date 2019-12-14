package com.hanaset.luke.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignRequest {

    @NotEmpty
    @Length(max = 16)
    @Pattern(regexp = "[a-zA-Z0-9]{1,16}", message = "ID는 최대 16자리로 영문자, 숫자만 가능합니다.")
    private String id;

    @NotEmpty
    @Pattern(regexp = "[0-9a-zA-Z!@#$%^&*]{10,50}", message = "Password는 10~50자리로 영문자, 숫자, 특수문자(!@#$%^&*)만 가능합니다.")
    @Length(min = 10, max = 50)
    private String password;
}
