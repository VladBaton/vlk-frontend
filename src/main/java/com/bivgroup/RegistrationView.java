package com.bivgroup;

import com.bivgroup.pojo.request.CreateAccountRequest;
import com.bivgroup.pojo.response.CreateAccountResponse;
import com.bivgroup.rest.BaseRestConnection;
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
import static com.bivgroup.constant.URL.CREATE_ACCOUNT_URL;

@Route("registration")
public class RegistrationView extends VerticalLayout {

    private final BaseRestConnection restConnection = new BaseRestConnection();

    public RegistrationView() {
        // Заголовок
        H1 title = new H1("Создание ЛК");
        add(title);

        // Поля ввода
        TextField dogNumber = new TextField("Номер договора");
        TextField loginField = new TextField("Логин");
        TextField passwordField = new TextField("Пароль");

        // Кнопки
        Button loginButton = new Button("Регистрация", event -> {
            CreateAccountRequest createAccountRequest = new CreateAccountRequest(dogNumber.getValue(),
                    loginField.getValue(), passwordField.getValue(), String.valueOf(UUID.randomUUID()),
                    String.valueOf(new Date()));

            CreateAccountResponse response = null;
            try {
                response = restConnection.post(createAccountRequest,
                        CREATE_ACCOUNT_URL,
                        null,
                        CreateAccountResponse.class);
            } catch (Exception ex) {
                Notification.show("Ошибка при регистрации: " + ex);
            }

            if (Objects.nonNull(response) && SUCCESS_STATUS_CODE.equals(response.getStatusCode())) {
                Notification.show("Аккаунт успешно создан", 2000, Notification.Position.MIDDLE);
                getUI().ifPresent(ui -> ui.navigate("login"));
            } else {
                Notification.show("Ошибка при регистрации", 2000, Notification.Position.MIDDLE);
            }
        });

        Button backButton = new Button("Назад", event -> {
            getUI().ifPresent(ui -> ui.navigate(""));
        });

        // Формат для полей ввода и кнопок
        FormLayout formLayout = new FormLayout();
        formLayout.add(dogNumber, loginField, passwordField, loginButton, backButton);
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