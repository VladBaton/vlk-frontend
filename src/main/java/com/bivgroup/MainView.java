package com.bivgroup;

import com.bivgroup.pojo.request.GetNotificationsByClientRequest;
import com.bivgroup.pojo.response.NotificationResponse;
import com.bivgroup.rest.BaseRestConnection;
import com.bivgroup.utils.Utils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    private final VLKApi vlkApi = new VLKApi();

    private final BaseRestConnection restConnection = new BaseRestConnection();

    public MainView() {
        Button button = new Button("Вызвать эндпоинт", event -> {
            try {
                GetNotificationsByClientRequest request = new GetNotificationsByClientRequest();
                request.setInsurerId(1L);
                NotificationResponse response = restConnection
                        .post(request, "http://localhost:8085/vlk/notifications/getByClient",
                                "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJ2bGstand0Iiwic3ViIjoidmxrLWp3dCIsImdyb3VwcyI6WyJhZG1pbiJdLCJleHAiOjE3NDQzOTkzNDM0MDEsImlhdCI6MTc0NDM5OTMzOSwianRpIjoiN2M3OWM2OTctYjY3Ni00ZGNjLTkxYmUtNWY4ZWJiNmFkYTE3In0.eHUfoyecVEGapMV78ogxAWK9XcP1eKD_tXweZHo_XhmxDfSWxOyI4Ngizc_IAQ2VmK1WZMR30C_rlW7dmbkzMzMLMWcRMzFnevGUhoDU9iX7kGKH0Hhl_FdZ6ADuxn0NzQYkJHxytdbB4JcDWen3VsKqAuecJcI-b8j-leJEsThVndYyCQWQZ8PdktLJNHOan4KfGdKhOh0YF3e9GJ0nioQEOOyrpaoTkATt-UPT79pQC_f_N7-Ya9QJezDhSUQG1yW_Ud9OXrqjoEGdA_xKrN56xcodX6T--EY2wtkdslu8HSLPRpSbIvZMhDXeZHc1jFoTAOrBlUY_l0hpzD1smQ",
                                NotificationResponse.class);
                Notification.show("Ответ сервиса: " + Utils.serialize(response));
            } catch (Exception e) {
                Notification.show("Ошибка: " + e);
            }
        });
        add(button);
    }
}

