package com.bivgroup.pojo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteAccountRequest extends BaseRequest {

    private String login;
}
