package com.bivgroup;

import com.bivgroup.pojo.request.CreateAccountRequest;
import com.bivgroup.pojo.request.ValidateVerificationCodeRequest;
import com.bivgroup.pojo.response.BaseResponse;
import com.bivgroup.pojo.response.CreateAccountResponse;
import com.bivgroup.rest.BaseRestConnection;
import com.bivgroup.utils.SessionUtils;
import com.bivgroup.utils.Utils;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static com.bivgroup.constant.Constants.*;
import static com.bivgroup.constant.URL.CREATE_ACCOUNT_URL;
import static com.bivgroup.constant.URL.VALIDATE_VERIFICATION_CODE_URL;

@Route("verificationCode")
public class VerificationCodeView extends VerticalLayout {

    private final BaseRestConnection restConnection = new BaseRestConnection();

    public VerificationCodeView() {

        this.setPadding(false); // Убираем внутренние отступы
        this.setSpacing(false); // Убираем промежутки между элементами
        this.getStyle().set("margin", "0"); // Убираем внешние отступы
        setSizeFull(); // Занимает всю доступную высоту

        // Логотип компании
        Image logo = new Image("logo.png", "Логотип компании"); // Путь к изображению
        logo.setHeight("61px"); // Установите нужную высоту

        // Заголовок с логотипом
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.add(logo);
        header.setWidthFull(); // Устанавливаем ширину на 100%
        header.setJustifyContentMode(JustifyContentMode.START); // Логотип слева


        String login = SessionUtils.getAttribute(USER_LOGIN_ATTRIBUTE);
        String password = SessionUtils.getAttribute(USER_PASSWORD_ATTRIBUTE);
        String dogNumber = SessionUtils.getAttribute(CONTRACT_NUMBER_ATTRIBUTE);

        // Поле для ввода кода из СМС
        PasswordField smsCodeField = new PasswordField("Введите проверочный код");
        smsCodeField.setRequiredIndicatorVisible(true);

        // Spacer для центрирования поля ввода
        Div spacer = new Div();
        spacer.setHeightFull(); // Занимает все доступное пространство
        spacer.setWidthFull(); // Занимает все доступное пространство

        // Кнопка "Ввод"
        Button submitButton = new Button("Далее", event -> {
            String code = smsCodeField.getValue();

            ValidateVerificationCodeRequest validationRequest = new ValidateVerificationCodeRequest();
            validationRequest.setVerificationCode(code);
            validationRequest.setContractNumber(dogNumber);
            BaseResponse validationResponse = new BaseResponse();

            try {
                validationResponse = restConnection.post(validationRequest,VALIDATE_VERIFICATION_CODE_URL, null, BaseResponse.class);
            }  catch (Exception ex) {
                Notification.show("Ошибка при проверке кода! ");
                return;
            }

            if (Objects.equals(validationResponse.getStatusCode(), 0L)) {
                createAccount(dogNumber, login, password);
            } else {
                Notification.show("Ошибка: " + Utils.encodeUTF8(validationResponse.getStatusDescription()));
            }
        });

        VerticalLayout smsCodeLayout = new VerticalLayout();
        smsCodeLayout.add(smsCodeField, submitButton);

        // Размещаем кнопки и поле ввода
        VerticalLayout centerLayout = new VerticalLayout(spacer, smsCodeField, submitButton, spacer);
        centerLayout.setAlignItems(Alignment.CENTER); // Центрируем содержимое по горизонтали
        centerLayout.setSpacing(true); // Добавляем промежутки между элементами

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

        add(header, centerLayout, spacer, footer);
    }

    private void createAccount(String dogNumber, String login, String password) {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest(dogNumber,
                login, password, String.valueOf(UUID.randomUUID()),
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
    }
}