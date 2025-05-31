package com.bivgroup;

import com.bivgroup.pojo.Contract;
import com.bivgroup.pojo.HandbookValue;
import com.bivgroup.pojo.Payment;
import com.bivgroup.pojo.request.GetUserDataRequest;
import com.bivgroup.pojo.response.GetUserDataResponse;
import com.bivgroup.rest.BaseRestConnection;
import com.bivgroup.utils.SessionUtils;
import com.bivgroup.utils.Utils;
import com.vaadin.flow.component.Text;
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

import java.util.*;

import static com.bivgroup.constant.Constants.FOOTER_TEXT;
import static com.bivgroup.constant.Constants.SUCCESS_STATUS_CODE;
import static com.bivgroup.constant.URL.GET_USER_DATA_URL;

@Route("insurerData")
public class InsurerDataView extends VerticalLayout implements BeforeEnterObserver {

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

    public InsurerDataView() {
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

        GetUserDataResponse response = null;

        try {
            response = restConnection.post(getUserDataRequest,
                    GET_USER_DATA_URL,
                    authToken,
                    GetUserDataResponse.class);
        } catch (Exception ex) {
            Notification.show("Ошибка при получении данных пользователя: " + ex);
            SessionUtils.clearAttribute("authToken");
            SessionUtils.clearAttribute("insurerId");
            getUI().ifPresent(ui -> ui.navigate(""));
        }

        if (Objects.nonNull(response) && SUCCESS_STATUS_CODE.equals(response.getStatusCode())) {
            H1 title = new H1("Данные страхователя");
            add(title);

            Button exitButton = new Button("Выйти", event -> {
                SessionUtils.clearAttribute("authToken");
                SessionUtils.clearAttribute("insurerId");
                getUI().ifPresent(ui -> ui.navigate(""));
            });
            Button toNotificationsButton = new Button
                    ("Уведомления Страхователя", event -> {
                        getUI().ifPresent(ui -> ui.navigate("insurerNotifications"));
                    });
            add(new HorizontalLayout(exitButton, toNotificationsButton));

            //Сами данные
            TextField nameField = new TextField("Имя");
            nameField.setValue(response.getInsurer().getInsurerName());
            nameField.setReadOnly(true);

            TextField surnameField = new TextField("Фамилия");
            surnameField.setValue(response.getInsurer().getInsurerSurname());
            surnameField.setReadOnly(true);

            TextField lastNameField = new TextField("Отчество");
            lastNameField.setValue(response.getInsurer().getInsurerLastName());
            lastNameField.setReadOnly(true);

            TextField emailField = new TextField("Email");
            emailField.setValue(response.getInsurer().getInsurerEmail());
            emailField.setReadOnly(true);

            TextField phoneField = new TextField("Телефон");
            phoneField.setValue(response.getInsurer().getInsurerPhoneNumber());
            phoneField.setReadOnly(true);

            // Создание выпадающего списка для выбора контракта
            ComboBox<Contract> contractComboBox = new ComboBox<>("Выберите контракт");
            contractComboBox.setItems(response.getInsurer().getContracts());
            contractComboBox.setItemLabelGenerator(Contract::getContractNumber);

            // Создание компонентов для отображения информации о выбранном контракте
            TextField contractNumberField = new TextField("Номер контракта");
            TextField startDateField = new TextField("Дата начала");
            TextField endDateField = new TextField("Дата окончания");

            // Создание таблицы для отображения платежей
            Grid<Payment> paymentGrid = new Grid<>(Payment.class);
            paymentGrid.setColumns("paymentId", "amount", "payDate", "orderNum", "status");
            paymentGrid.getColumnByKey("amount").setHeader("Сумма платежа");
            paymentGrid.getColumnByKey("payDate").setHeader("Срок оплаты");
            paymentGrid.getColumnByKey("orderNum").setHeader("Номер платежа");
            paymentGrid.getColumnByKey("status").setHeader("Статус платежа");
            paymentGrid.getColumnByKey("paymentId").setVisible(false);

            VerticalLayout contractExtInfo = new VerticalLayout();
            contractExtInfo.setSizeFull();
            contractExtInfo.setAlignItems(Alignment.CENTER);

            // Обработка выбора контракта
            contractComboBox.addValueChangeListener(event -> {
                Contract selectedContract = event.getValue();
                if (selectedContract != null) {
                    contractNumberField.setValue(selectedContract.getContractNumber());
                    startDateField.setValue(Utils.formatDate(selectedContract.getStartDate()));
                    endDateField.setValue(Utils.formatDate(selectedContract.getEndDate()));

                    contractExtInfo.removeAll(); // очистка доп инфы
                    int numberOfFieldOnRow = 0;
                    HorizontalLayout contractExtRow = new HorizontalLayout();
                    contractExtRow.setSizeFull();
                    contractExtRow.setAlignItems(Alignment.CENTER);
                    //Заполнение доп инфы о контракте:
                    for (Map.Entry<String, HandbookValue> entry: selectedContract.getContractExt().entrySet()) {
                        TextField textField = new TextField(entry.getValue().getDescription());
                        textField.setValue(entry.getValue().getValue().toString());
                        textField.setTitle(entry.getValue().getDescription());
                        textField.setReadOnly(true);
                        textField.setWidthFull();
                        contractExtRow.add(textField);
                        if ( ++numberOfFieldOnRow > 2) { // на одной строке размещается три доп инфы
                            contractExtInfo.add(contractExtRow);
                            contractExtRow = new HorizontalLayout();
                            contractExtRow.setSizeFull();
                            contractExtRow.setAlignItems(Alignment.CENTER);
                            numberOfFieldOnRow = 0;
                        }
                    }

                    // Заполнение таблицы платежей
                    paymentGrid.setItems(selectedContract.getPayments());
                } else {
                    contractNumberField.clear();
                    startDateField.clear();
                    endDateField.clear();
                    contractExtInfo.removeAll(); // очистка доп инфы
                    paymentGrid.setItems(List.of()); // Очистка таблицы
                }
            });


            // Добавление всех компонентов на страницу
            add(new HorizontalLayout(nameField, surnameField, lastNameField, emailField, phoneField),
                    new HorizontalLayout(contractComboBox, contractNumberField, startDateField, endDateField),
                    contractExtInfo,
                    paymentGrid);

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
        } else {
            Notification.show("Ошибка...", 2000, Notification.Position.MIDDLE);
        }
    }
}