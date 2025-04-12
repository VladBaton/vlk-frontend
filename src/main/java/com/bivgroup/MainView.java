package com.bivgroup;

import com.bivgroup.rest.BaseRestConnection;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        // Заголовок
        H1 title = new H1("Виртуальный личный кабинет страхователя");
        add(title);

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
        add(buttonLayout);

        // Подвал
        Footer footer = new Footer();
        Div footerContent = new Div();
        footerContent.setText("Business intelligence vision, 2025");
        footer.add(footerContent);
        add(footer);

        // Полное центрирование
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull(); // Занимает всю доступную высоту
    }
}

