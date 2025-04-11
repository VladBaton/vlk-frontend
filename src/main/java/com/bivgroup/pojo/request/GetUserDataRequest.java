package com.bivgroup.pojo.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserDataRequest extends BaseRequest {

    @NotNull
    @Positive
    private Long insurerId;
}
