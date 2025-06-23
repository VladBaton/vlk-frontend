package com.bivgroup.pojo;

import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;

@Getter
@Setter
public class NotificationStrings {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm dd.MM.yyyy");

    public NotificationStrings(Notification notification) {
        notificationId = notification.getNotificationId();
        contractId = notification.getContractId();
        insurerId = notification.getInsurerId();
        eventCode = notification.getEventCode();
        message = notification.getMessage();
        createDate = simpleDateFormat.format(notification.getCreateDate());
    }

    private Long notificationId;
    private Long contractId;
    private Long insurerId;
    private Long eventCode;
    private String message;
    private String createDate;
}
