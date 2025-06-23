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

import static com.bivgroup.constant.Constants.*;
import static com.bivgroup.constant.URL.CREATE_ACCOUNT_URL;
import static com.bivgroup.constant.URL.CREATE_VERIFICATION_CODE_URL;

@Route("registration")
public class RegistrationView extends VerticalLayout {

    private final BaseRestConnection restConnection = new BaseRestConnection();

    public RegistrationView() {

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
        H1 title = new H1("Создание ЛК");
        add(spacer, title);

        // Поля ввода
        TextField dogNumber = new TextField("Номер договора");
        dogNumber.setRequired(true);
        TextField loginField = new TextField("Логин");
        loginField.setRequired(true);
        PasswordField passwordField = new PasswordField("Пароль");
        passwordField.setRequired(true);

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

        Button backButton = new Button("Назад", event -> getUI().ifPresent(ui -> ui.navigate("")));

        // Формат для полей ввода и кнопок
        FormLayout formLayout = new FormLayout();
        formLayout.add(dogNumber, loginField, passwordField);
        add(formLayout, new HorizontalLayout(loginButton, backButton));

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
        add(spacer, footer);

        // Полное центрирование
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull(); // Занимает всю доступную высоту
    }
}