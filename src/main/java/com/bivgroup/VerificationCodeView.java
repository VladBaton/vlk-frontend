package com.bivgroup;

import com.bivgroup.pojo.request.CreateAccountRequest;
import com.bivgroup.pojo.request.ValidateVerificationCodeRequest;
import com.bivgroup.pojo.response.BaseResponse;
import com.bivgroup.pojo.response.CreateAccountResponse;
import com.bivgroup.rest.BaseRestConnection;
import com.bivgroup.utils.SessionUtils;
import com.bivgroup.utils.Utils;
import com.vaadin.flow.component.button.Button;
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

        String login = SessionUtils.getAttribute(USER_LOGIN_ATTRIBUTE);
        String password = SessionUtils.getAttribute(USER_PASSWORD_ATTRIBUTE);
        String dogNumber = SessionUtils.getAttribute(CONTRACT_NUMBER_ATTRIBUTE);

        // Поле для ввода кода из СМС
        PasswordField smsCodeField = new PasswordField("Введите проверочный код");
        smsCodeField.setRequiredIndicatorVisible(true);

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

        // Добавляем компоненты на страницу
        add(smsCodeField, submitButton);
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