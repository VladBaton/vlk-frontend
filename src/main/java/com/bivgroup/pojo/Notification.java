package com.bivgroup.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notification {

    private Long notificationId;
    private Long contractId;
    private Long insurerId;
    private Long eventCode;
    private String message;
}
