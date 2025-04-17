package com.bivgroup.pojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAccountRequest extends BaseRequest {

    @NotNull
    @NotBlank
    @Size(min = 3, max = 64)
    private String userLogin;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 64)
    private String insurerName;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 64)
    private String insurerSurname;

    private String insurerLastName;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]+@[a-zA-Z][a-zA-Z0-9]+.[a-zA-Z]{1,3}$")
    private String insurerEmail;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 64)
    private String insurerPhoneNumber;
}
