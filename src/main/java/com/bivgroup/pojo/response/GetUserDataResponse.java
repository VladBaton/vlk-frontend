package com.bivgroup.pojo.response;

import com.bivgroup.pojo.Insurer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUserDataResponse extends BaseResponse {

    Insurer insurer;
}
