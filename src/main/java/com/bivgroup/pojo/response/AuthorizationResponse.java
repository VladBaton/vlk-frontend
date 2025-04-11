package com.bivgroup.pojo.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorizationResponse extends BaseResponse {

    private String token;

    public AuthorizationResponse(String token) {
        this.token = token;
    }
}
