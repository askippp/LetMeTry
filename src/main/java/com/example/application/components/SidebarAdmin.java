package com.example.application.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.server.VaadinSession;

public class SidebarAdmin {

    public static VerticalLayout createSidebar() {
        VerticalLayout sidebar = new VerticalLayout();
        sidebar.setWidth("250px");
        sidebar.setHeightFull();
        sidebar.setPadding(false);
        sidebar.setSpacing(false);
        sidebar.getStyle()
                .setBackgroundColor("#D3F1DF")
                .setBorderRight("1px solid #e2e8f0");

        Button usersBtn = createNavButton("Users", "images/dashboard.png", "users");
        Button latsolBtn = createNavButton("Latihan Soal", "images/historyicon.png", "latihan-soal");
        Button tryoutBtn = createNavButton("Try Out", "images/rank.png", "try-out-easy");

        usersBtn.getStyle().setMarginTop("40px");

        Image logoutIcon = new Image("images/logout.png", "Logout");
        logoutIcon.setWidth("30px");
        logoutIcon.setHeight("30px");
        logoutIcon.getStyle().set("margin-right", "8px");

        Span logoutText = new Span("Logout");
        logoutText.getStyle()
                .set("color", "white")
                .set("font-weight", "500");

        HorizontalLayout logoutContent = new HorizontalLayout(logoutIcon, logoutText);
        logoutContent.setAlignItems(FlexComponent.Alignment.CENTER);
        logoutContent.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        logoutContent.setSpacing(false);
        logoutContent.setPadding(false);

        Button logoutBtn = new Button(logoutContent);
        logoutBtn.setWidth("200px");
        logoutBtn.getStyle()
                .setMarginLeft("25px")
                .setMarginRight("25px")
                .setMarginTop("auto")
                .setMarginBottom("20px")
                .setBorderRadius("8px")
                .setFontWeight("500")
                .setBackgroundColor("#dc2626")
                .setColor("white")
                .setBorder("none")
                .setPadding("12px 16px");

        logoutBtn.addClickListener(buttonClickEvent -> {
            VaadinSession.getCurrent().getSession().invalidate();
            UI.getCurrent().navigate("");
        });

        sidebar.add(usersBtn, latsolBtn, tryoutBtn, logoutBtn);

        return sidebar;
    }

    public static Button createNavButton(String text, String imagePath, String route) {
        VerticalLayout iconContainer = new VerticalLayout();
        iconContainer.setWidth("32px");
        iconContainer.setHeight("32px");
        iconContainer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        iconContainer.setAlignItems(FlexComponent.Alignment.CENTER);
        iconContainer.setPadding(false);
        iconContainer.setSpacing(false);
        iconContainer.getStyle().set("flex-shrink", "0");

        Image icon = new Image(imagePath, text);
        icon.setWidth("24px");
        icon.setHeight("24px");

        iconContainer.add(icon);

        Span label = new Span(text);
        label.getStyle()
                .set("font-size", "16px")
                .set("font-weight", "500")
                .set("color", "#374151");

        HorizontalLayout content = new HorizontalLayout(iconContainer, label);
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        content.setSpacing(true);
        content.setPadding(false);
        content.setWidthFull();

        Button button = new Button(content);
        button.setWidthFull();
        button.getStyle()
                .setBackground("transparent")
                .setBorder("none")
                .setPadding("16px 24px")
                .setTextAlign(Style.TextAlign.LEFT)
                .setCursor("pointer")
                .setBorderRadius("0");

        button.addClickListener(buttonClickEvent -> {
            UI.getCurrent().navigate(route);
        });

        return button;
    }
}
