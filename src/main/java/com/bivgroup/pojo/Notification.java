package com.bivgroup.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Notification {

    private Long notificationId;
    private Long contractId;
    private Long insurerId;
    private Long eventCode;
    private String message;
    private Date createDate;
}
