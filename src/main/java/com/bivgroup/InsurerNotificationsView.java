package com.bivgroup;

import com.bivgroup.pojo.Contract;
import com.bivgroup.pojo.Payment;
import com.bivgroup.pojo.request.GetNotificationsByClientRequest;
import com.bivgroup.pojo.request.GetNotificationsByContractNumberRequest;
import com.bivgroup.pojo.request.GetUserDataRequest;
import com.bivgroup.pojo.response.GetUserDataResponse;
import com.bivgroup.pojo.response.NotificationResponse;
import com.bivgroup.rest.BaseRestConnection;
import com.bivgroup.utils.SessionUtils;
import com.bivgroup.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.bivgroup.constant.Constants.FOOTER_TEXT;
import static com.bivgroup.constant.Constants.SUCCESS_STATUS_CODE;
import static com.bivgroup.constant.URL.*;

@Route("insurerNotifications")
public class InsurerNotificationsView extends VerticalLayout implements BeforeEnterObserver {

    private final BaseRestConnection restConnection = new BaseRestConnection();

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Проверяем, есть ли токен в сессии
        String authToken = SessionUtils.getAttribute("authToken");
        if (StringUtils.isEmpty(authToken)) {
            // Если токен отсутствует, перенаправляем на начальную страницу
            Notification.show("Для просмотра данных пользователя необходима авторизация");
            event.forwardTo("");
        }
    }

    public InsurerNotificationsView() {
        String authToken = SessionUtils.getAttribute("authToken");
        Long insurerId = null;
        try {
            insurerId = Long.parseLong(SessionUtils.getAttribute("insurerId"));
        } catch (NumberFormatException ex) {
            Notification.show("Ошибка при чтении insurerId");
            SessionUtils.clearAttribute("authToken");
            SessionUtils.clearAttribute("insurerId");
            getUI().ifPresent(ui -> ui.navigate(""));
            return;
        }

        GetUserDataRequest getUserDataRequest = new GetUserDataRequest();
        getUserDataRequest.setInsurerId(insurerId);
        getUserDataRequest.setRqId(String.valueOf(UUID.randomUUID()));
        getUserDataRequest.setRqTm(String.valueOf(new Date()));

        GetUserDataResponse getUserDataResponse = null;

        try {
            getUserDataResponse = restConnection.post(getUserDataRequest,
                    GET_USER_DATA_URL,
                    authToken,
                    GetUserDataResponse.class);
        } catch (Exception ex) {
            Notification.show("Ошибка при получении данных пользователя: " + ex);
            SessionUtils.clearAttribute("authToken");
            SessionUtils.clearAttribute("insurerId");
            getUI().ifPresent(ui -> ui.navigate(""));
        }

        if (Objects.nonNull(getUserDataResponse) && SUCCESS_STATUS_CODE.equals(getUserDataResponse.getStatusCode())) {
            H1 title = new H1("Данные страхователя");
            add(title);

            Button exitButton = new Button("Выйти", event -> {
                SessionUtils.clearAttribute("authToken");
                SessionUtils.clearAttribute("insurerId");
                getUI().ifPresent(ui -> ui.navigate(""));
            });

            Button toUserDataButton = new Button("Данные пользователя", event -> {
                getUI().ifPresent(ui -> ui.navigate("insurerData"));
            });
            add(new HorizontalLayout(exitButton, toUserDataButton));

            //Сами данные

            /*// Создание таблицы для отображения уведомлений
            Grid<com.bivgroup.pojo.Notification> notificationsGrid = new Grid<>(com.bivgroup.pojo.Notification.class);
            notificationsGrid.setColumns("notificationId", "message");

            GetNotificationsByClientRequest request = new GetNotificationsByClientRequest();
            request.setInsurerId(insurerId);
            request.setRqId(String.valueOf(UUID.randomUUID()));
            request.setRqTm(String.valueOf(new Date()));


            NotificationResponse notificationResponse = null;
            try {
                notificationResponse = restConnection.post(request,
                        GET_NOTIFICATIONS_BY_INSURER_ID,
                        authToken,
                        NotificationResponse.class);
            } catch (Exception ex) {
                Notification.show("Ошибка при получении уведомлений");
            }

             if (Objects.nonNull(notificationResponse) && Objects.nonNull(notificationResponse.getInsurerNotifications()) &&
                     !notificationResponse.getInsurerNotifications().isEmpty()
                     && SUCCESS_STATUS_CODE.equals(notificationResponse.getStatusCode())) {
                 notificationsGrid.setItems(notificationResponse.getInsurerNotifications());
             }*/



            // Создание выпадающего списка для выбора контракта
            ComboBox<Contract> contractComboBox = new ComboBox<>("Выберите контракт");
            contractComboBox.setItems(getUserDataResponse.getInsurer().getContracts());
            contractComboBox.setItemLabelGenerator(Contract::getContractNumber);

            // Создание компонентов для отображения информации о выбранном контракте
            TextField contractNumberField = new TextField("Номер контракта");
            TextField startDateField = new TextField("Дата начала");
            TextField endDateField = new TextField("Дата окончания");

            // Создание таблицы для отображения уведомлений
            Grid<com.bivgroup.pojo.Notification> notificationsGrid = new Grid<>(com.bivgroup.pojo.Notification.class);
            notificationsGrid.setColumns("notificationId", "message", "createDate");
            notificationsGrid.getColumnByKey("notificationId").setVisible(false);
            notificationsGrid.getColumnByKey("message").setHeader("Текст уведомления");
            notificationsGrid.getColumnByKey("createDate").setHeader("Дата");

            // Обработка выбора контракта
            contractComboBox.addValueChangeListener(event -> {
                Contract selectedContract = event.getValue();
                if (selectedContract != null) {
                    contractNumberField.setValue(selectedContract.getContractNumber());
                    startDateField.setValue(Utils.formatDate(selectedContract.getStartDate()));
                    endDateField.setValue(Utils.formatDate(selectedContract.getEndDate()));

                    GetNotificationsByContractNumberRequest getNotificationsByContractNumberRequest = new GetNotificationsByContractNumberRequest();
                    if (Objects.isNull(selectedContract.getContractNumber()) || StringUtils.isEmpty(selectedContract.getContractNumber())) {
                        Notification.show("Поле пусто!");
                    }
                    getNotificationsByContractNumberRequest.setContractNumber(selectedContract.getContractNumber());
                    getNotificationsByContractNumberRequest.setRqId(String.valueOf(UUID.randomUUID()));
                    getNotificationsByContractNumberRequest.setRqTm(String.valueOf(new Date()));

                    try {
                        Notification.show(new ObjectMapper().writeValueAsString(getNotificationsByContractNumberRequest));
                    } catch (JsonProcessingException e) {

                    }
                    NotificationResponse notificationResponse = null;
                    try {
                        notificationResponse = restConnection.post(getNotificationsByContractNumberRequest,
                                GET_NOTIFICATIONS_BY_CONTRACT_NUMBER,
                                authToken,
                                NotificationResponse.class);
                    } catch (Exception ex) {
                        Notification.show("Ошибка при получении уведомлений для контракта " + selectedContract.getContractNumber()
                        + ": " + ex);
                    }
                    if (Objects.nonNull(notificationResponse)) {
                        notificationsGrid.setItems(notificationResponse.getInsurerNotifications());
                    }

                } else {
                    contractNumberField.clear();
                    startDateField.clear();
                    endDateField.clear();
                    notificationsGrid.setItems(List.of());
                }
            });

            // Добавление всех компонентов на страницу
            add(new HorizontalLayout(contractComboBox, contractNumberField, startDateField, endDateField, notificationsGrid));

            add(notificationsGrid);

            // Установка остальных свойств
            setSizeFull();
            setPadding(true);
            setSpacing(true);


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

}
