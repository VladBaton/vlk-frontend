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

        if (notification.getEventCode() == 1L) {
            eventCode = "Создание договора";
        } else if (notification.getEventCode() == 2L) {
            eventCode = "Начало действия договора";
        } else if (notification.getEventCode() == 3L) {
            eventCode = "Окончание действия договора";
        } else if (notification.getEventCode() == 4L) {
            eventCode = "Расторжение договора";
        } else if (notification.getEventCode() == 5L) {
            eventCode = "Оплата очередного периода";
        } else if (notification.getEventCode() == 6L) {
            eventCode = "Напоминание о необходимости внесения страховой премии";
        }

        message = notification.getMessage();
        createDate = simpleDateFormat.format(notification.getCreateDate());
    }

    private Long notificationId;
    private Long contractId;
    private Long insurerId;
    private String eventCode;
    private String message;
    private String createDate;
}
