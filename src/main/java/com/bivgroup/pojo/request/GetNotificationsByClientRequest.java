package com.bivgroup.pojo.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetNotificationsByClientRequest extends BaseRequest {

    @NotNull
    @Positive
    private Long insurerId;
}
