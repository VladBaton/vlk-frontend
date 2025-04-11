package org.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    private final VLKApi vlkApi = new VLKApi();

    public MainView() {
        Button button = new Button("Вызвать эндпоинт", event -> {
            try {
                String response = vlkApi.callHelloEndpoint();
                Notification.show("Ответ сервиса: " + response);
            } catch (Exception e) {
                Notification.show("Ошибка: " + e);
            }
        });
        add(button);
    }
}

