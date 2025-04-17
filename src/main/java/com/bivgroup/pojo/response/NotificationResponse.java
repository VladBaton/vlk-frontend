package com.bivgroup.pojo.response;

import com.bivgroup.pojo.Notification;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NotificationResponse extends BaseResponse {

    private Long insurerId;
    private List<Notification> insurerNotifications;
}
