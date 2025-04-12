package com.bivgroup.pojo.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountRequest extends BaseRequest {

    @NotNull
    @NotBlank
    @Size(min = 3, max = 16)
    private String contractNumber;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 128)
    private String login;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 16)
    private String password;

    @JsonCreator
    public CreateAccountRequest(@JsonProperty("contractNumber") String contractNumber,
                                @JsonProperty("login") String login,
                                @JsonProperty("password") String password,
                                @JsonProperty("rqId") String rqId,
                                @JsonProperty("rqTm") String rqTm) {
        this.contractNumber = contractNumber;
        this.login = login;
        this.password = password;
        setRqTm(rqTm);
        setRqId(rqId);
    }
}
