package com.bivgroup.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateVerificationCodeRequest extends BaseRequest {

    @NotNull
    @NotBlank
    String contractNumber;

    @NotNull
    @NotBlank
    String verificationCode;
}
