package com.bivgroup.pojo.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAccountResponse extends BaseResponse {

    private String userLogin;
    private String[] changedFields;
}
