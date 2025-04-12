package com.bivgroup.pojo.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorizationResponse extends BaseResponse {

    private Long insurerId;
    private String token;

    @JsonCreator
    public AuthorizationResponse(@JsonProperty("insurerId") Long insurerId, @JsonProperty("token") String token) {
        this.insurerId = insurerId;
        this.token = token;
    }
}
