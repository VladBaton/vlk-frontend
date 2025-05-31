package com.bivgroup;

import com.bivgroup.pojo.request.AuthorizationRequest;
import com.bivgroup.pojo.request.DeleteAccountRequest;
import com.bivgroup.pojo.response.AuthorizationResponse;
import com.bivgroup.pojo.response.BaseResponse;
import com.bivgroup.rest.BaseRestConnection;
import com.bivgroup.utils.SessionUtils;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import static com.bivgroup.constant.Constants.*;
import static com.bivgroup.constant.URL.AUTHORIZATION_URL;
import static com.bivgroup.constant.URL.DELETE_USER_ACCOUNT;


@Route("resetPassword")
public class ResetPasswordView extends VerticalLayout {

    private final BaseRestConnection restConnection = new BaseRestConnection();

    public ResetPasswordView() {

        // Заголовок страницы
        add(new H1("Подтвердите удаление учётной записи"));

        // Подсказка для ввода пароля
        add(new Text("Для продолжения введите пароль и нажмите 'Далее'. " +
                "После сброса пароля вам придётся пройти процедуру создания личного кабинета заново!"));

        // Поле для ввода предыдущего пароля
        PasswordField previousPasswordField = new PasswordField("Введите пароль");
        previousPasswordField.setRequired(true);  // Устанавливаем необходимость заполнения поля

        // Кнопка "Далее"
        Button nextButton = new Button("Далее", event -> {
            String previousPassword = previousPasswordField.getValue();
            String userLogin = SessionUtils.getAttribute(USER_LOGIN_ATTRIBUTE);
            String authToken = SessionUtils.getAttribute(AUTH_TOKEN_ATTRIBUTE);
            // Логика проверки предыдущего пароля
            if (isValidPassword(userLogin, previousPassword)) {
                DeleteAccountRequest request = new DeleteAccountRequest();
                request.setLogin(userLogin);
                request.setRqId(String.valueOf(UUID.randomUUID()));
                request.setRqTm(String.valueOf(new Date()));
                BaseResponse response;
                try {
                    response = restConnection.post(request, DELETE_USER_ACCOUNT, authToken, BaseResponse.class);
                } catch (Exception ex) {
                    Notification.show("Непредвиденная ошибка при удалении учётной записи: " + ex);
                    return;
                }

                Notification.show("Учётная запись удалена!");
                SESSION_ATTRIBUTES.forEach(SessionUtils::clearAttribute);
                getUI().ifPresent(ui -> ui.navigate(""));
            } else {
                Notification.show("Неверный пароль. Попробуйте снова.");
            }
        });

        // Кнопка "Нет"
        Button noButton = new Button("Назад", event -> {
            Notification.show("Сброс пароля отменен.");
            getUI().ifPresent(ui -> ui.navigate("insurerData"));
        });

        // Добавляем все элементы на страницу
        add(previousPasswordField, new HorizontalLayout(nextButton, noButton));

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

    private boolean isValidPassword(String login, String password) {
        AuthorizationRequest request = new AuthorizationRequest();
        request.setUserLogin(login);
        request.setPassword(password);
        request.setRqId(String.valueOf(UUID.randomUUID()));
        request.setRqTm(String.valueOf(new Date()));
        AuthorizationResponse response = null;
        try {
            response = restConnection.post(request, AUTHORIZATION_URL, null, AuthorizationResponse.class);
        } catch (Exception ex) {
            Notification.show("Ошибка при проверке данных пользователя. Повторите запрос позже: " + ex);
        }
        return (Objects.nonNull(response) && SUCCESS_STATUS_CODE.equals(response.getStatusCode()));
    }
}
