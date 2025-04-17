package com.bivgroup;

import com.bivgroup.pojo.request.AuthorizationRequest;
import com.bivgroup.pojo.response.AuthorizationResponse;
import com.bivgroup.rest.BaseRestConnection;
import com.bivgroup.utils.SessionUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static com.bivgroup.constant.Constants.FOOTER_TEXT;
import static com.bivgroup.constant.Constants.SUCCESS_STATUS_CODE;
import static com.bivgroup.constant.URL.AUTHORIZATION_URL;

@Route("login")
public class LoginView extends VerticalLayout {

    private final BaseRestConnection restConnection = new BaseRestConnection();

    public LoginView() {
        // Заголовок
        H1 title = new H1("Авторизация");
        add(title);

        // Поля ввода
        TextField loginField = new TextField("Логин");
        TextField passwordField = new TextField("Пароль");

        // Кнопки
        Button loginButton = new Button("Вход", event -> {
            AuthorizationRequest request = new AuthorizationRequest();
            request.setUserLogin(loginField.getValue());
            request.setPassword(passwordField.getValue());
            request.setRqId(String.valueOf(UUID.randomUUID()));
            request.setRqTm(String.valueOf(new Date()));
            AuthorizationResponse response = null;
            try {
                response = restConnection.post(request, AUTHORIZATION_URL, null, AuthorizationResponse.class);
            } catch (Exception ex) {
                Notification.show("Ошибка при авторизации: " + ex);
            }
            if (Objects.nonNull(response) && SUCCESS_STATUS_CODE.equals(response.getStatusCode())) {
                Notification.show("Вы успешно вошли", 2000, Notification.Position.MIDDLE);
                SessionUtils.saveAttribute("authToken", response.getToken());
                SessionUtils.saveAttribute("insurerId", String.valueOf(response.getInsurerId()));

                // Перенаправление на страницу данных
                getUI().ifPresent(ui -> ui.navigate("insurerData"));
            } else {
                Notification.show("Неверный логин или пароль", 2000, Notification.Position.MIDDLE);
            }
        });

        Button backButton = new Button("Назад", event -> {
            getUI().ifPresent(ui -> ui.navigate(""));
        });

        // Формат для полей ввода и кнопок
        FormLayout formLayout = new FormLayout();
        formLayout.add(loginField, passwordField, loginButton, backButton);
        add(formLayout);

        // Подвал
        Footer footer = new Footer();
        Div footerContent = new Div();
        footerContent.setText(FOOTER_TEXT);
        footer.add(footerContent);
        add(footer);

        // Полное центрирование
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull(); // Занимает всю доступную высоту
    }
}