package com.bivgroup;

import com.bivgroup.rest.BaseRestConnection;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {

        this.setSpacing(true);

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

        // Заголовок
        H1 title = new H1("Виртуальный личный кабинет страхователя");

        // Кнопки
        Button loginButton = new Button("Войти", event -> {
            getUI().ifPresent(ui -> ui.navigate("login")); // Перенаправление на страницу авторизации
        });
        Button registerButton = new Button("Регистрация", event -> {
            getUI().ifPresent(ui -> ui.navigate("registration"));
        });

        // Вертикальный Layout для кнопок
        VerticalLayout buttonLayout = new VerticalLayout(loginButton, registerButton);
        buttonLayout.setAlignItems(Alignment.CENTER);

        // Размещаем кнопки и поле ввода
        VerticalLayout centerLayout = new VerticalLayout(title, buttonLayout, spacer);
        centerLayout.setAlignItems(Alignment.CENTER); // Центрируем содержимое по горизонтали
        centerLayout.setSpacing(true); // Добавляем промежутки между элементами

        // Подвал
        Footer footer = new Footer();
        Div footerContent = new Div();
        footerContent.setText("Business intelligence vision, 2025");
        footer.add(footerContent);
        footer.getStyle().set("text-align", "center");
        footer.getStyle().set("position", "fixed");
        footer.getStyle().set("bottom", "0");
        footer.getStyle().set("left", "0");
        footer.getStyle().set("width", "100%");
        footer.setWidthFull();
        this.setSpacing(true);
        add(header, spacer, centerLayout, spacer, footer);

        // Полное центрирование
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull(); // Занимает всю доступную высоту
    }
}

