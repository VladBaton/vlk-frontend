package com.bivgroup;

import com.bivgroup.pojo.request.AuthorizationRequest;
import com.bivgroup.pojo.response.AuthorizationResponse;
import com.bivgroup.rest.BaseRestConnection;
import com.bivgroup.utils.SessionUtils;
import com.bivgroup.utils.Utils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
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

        // Логотип компании
        Image logo = new Image("logo.png", "Логотип компании"); // Путь к изображению
        logo.setHeight("61px"); // Установите нужную высоту

        // Заголовок с логотипом
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.add(logo);
        header.setWidthFull(); // Устанавливаем ширину на 100%
        header.setJustifyContentMode(JustifyContentMode.START); // Логотип слева

        // Spacer для центрирования поля ввода
        Div spacer = new Div();
        spacer.setHeightFull(); // Занимает все доступное пространство
        spacer.setWidthFull(); // Занимает все доступное пространство

        add(header);


        // Заголовок
        H1 title = new H1("Авторизация");
        add(spacer, new Div(), new Div(), title);

        // Поля ввода
        TextField loginField = new TextField("Логин");
        loginField.setRequired(true);
        PasswordField passwordField = new PasswordField("Пароль");
        passwordField.setRequired(true);

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
                Notification.show(Utils.encodeUTF8("Ошибка при авторизации: " + ex));
            }
            if (Objects.nonNull(response) && SUCCESS_STATUS_CODE.equals(response.getStatusCode())) {
                Notification.show("Вы успешно вошли в личный кабинет", 2000, Notification.Position.MIDDLE);
                SessionUtils.saveAttribute("authToken", response.getToken());
                SessionUtils.saveAttribute("userLogin", loginField.getValue());
                SessionUtils.saveAttribute("insurerId", String.valueOf(response.getInsurerId()));

                // Перенаправление на страницу данных
                getUI().ifPresent(ui -> ui.navigate("insurerData"));
            } else if (Objects.nonNull(response)){
                Notification.show(Utils.encodeUTF8(response.getStatusDescription()), 2000, Notification.Position.MIDDLE);
            } else {
                Notification.show(Utils.encodeUTF8("Неверный логин или пароль"), 2000, Notification.Position.MIDDLE);
            }
        });

        Button backButton = new Button("Назад", event -> {
            getUI().ifPresent(ui -> ui.navigate(""));
        });

        // Формат для полей ввода и кнопок
        FormLayout formLayout = new FormLayout();
        formLayout.add(loginField, passwordField, loginButton, backButton);
        add(formLayout, spacer);

        // Подвал
        Footer footer = new Footer();
        Div footerContent = new Div();
        footerContent.setText(FOOTER_TEXT);
        footer.add(footerContent);
        footer.getStyle().set("text-align", "center");
        footer.getStyle().set("position", "fixed");
        footer.getStyle().set("bottom", "0");
        footer.getStyle().set("left", "0");
        footer.getStyle().set("width", "100%");
        footer.setWidthFull();
        add(footer);

        // Полное центрирование
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull(); // Занимает всю доступную высоту
    }
}