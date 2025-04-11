package com.bivgroup.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorizationRequest extends BaseRequest {

    @NotNull
    @NotBlank
    @Size(min = 3, max = 64)
    private String userLogin;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 16)
    private String password;
}
