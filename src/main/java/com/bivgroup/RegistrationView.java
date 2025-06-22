package com.bivgroup;

import com.bivgroup.pojo.request.CreateAccountRequest;
import com.bivgroup.pojo.request.CreateVerificationCodeRequest;
import com.bivgroup.pojo.response.BaseResponse;
import com.bivgroup.pojo.response.CreateAccountResponse;
import com.bivgroup.rest.BaseRestConnection;
import com.bivgroup.utils.SessionUtils;
import com.bivgroup.utils.Utils;
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

import static com.bivgroup.constant.Constants.*;
import static com.bivgroup.constant.URL.CREATE_ACCOUNT_URL;
import static com.bivgroup.constant.URL.CREATE_VERIFICATION_CODE_URL;

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
            CreateVerificationCodeRequest request = new CreateVerificationCodeRequest();
            request.setContractNumber(dogNumber.getValue());
            BaseResponse response = new BaseResponse();
            
            try {
                response = restConnection.post(request, CREATE_VERIFICATION_CODE_URL, null, BaseResponse.class);
            } catch (Exception ex) {
                Notification.show("Ошибка при регистрации");
            }

            if (Objects.equals(response.getStatusCode(), 0L)) {
                SessionUtils.saveAttribute(USER_LOGIN_ATTRIBUTE, loginField.getValue());
                SessionUtils.saveAttribute(USER_PASSWORD_ATTRIBUTE, passwordField.getValue());
                SessionUtils.saveAttribute(CONTRACT_NUMBER_ATTRIBUTE, dogNumber.getValue());
                getUI().ifPresent(ui -> ui.navigate("verificationCode"));
            } else {
                Notification.show("Ошибка при регистрации!" + Utils.encodeUTF8(response.getStatusDescription()));
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