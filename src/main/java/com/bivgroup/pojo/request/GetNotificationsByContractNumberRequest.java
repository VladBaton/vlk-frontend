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
public class GetNotificationsByContractNumberRequest extends BaseRequest {

    @NotNull
    @NotBlank
    @Size(min = 3, max = 16)
    private String contractNumber;

    public GetNotificationsByContractNumberRequest() {}

    @JsonCreator
    public GetNotificationsByContractNumberRequest(@JsonProperty("contractNumber") String contractNumber) {
        this.contractNumber = contractNumber;
    }
}
