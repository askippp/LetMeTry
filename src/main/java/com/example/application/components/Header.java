package com.example.application.components;

import com.example.application.model.UsersModel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.server.VaadinSession;
import org.apache.catalina.User;

public class Header {

    public static HorizontalLayout headerWithLogo() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setPadding(true);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.getStyle()
                .setBackgroundColor("white")
                .setBoxShadow("0 2px 4px rgba(0,0,0,0.1)")
                .setPosition(Style.Position.STICKY)
                .setTop("0")
                .setZIndex(1000);

        HorizontalLayout brandLayout = new HorizontalLayout();
        brandLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        Image logoIcon = new Image();
        logoIcon.setSrc("images/logo.png");
        logoIcon.setAlt("Logo LetMeTry");
        logoIcon.getStyle()
                .setWidth("60px")
                .setHeight("60px")
                .set("object-fit", "contain")
                .setPosition(Style.Position.ABSOLUTE);

        Span brandName = new Span("LetMeTry");
        brandName.addClassName("kavoon-text");
        brandName.getStyle()
                .setFontSize("25px")
                .setFontWeight("bold")
                .setColor("#16C47F")
                .setMarginLeft("70px");

        brandLayout.add(logoIcon, brandName);

        HorizontalLayout userLayout = new HorizontalLayout();
        userLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        userLayout.getStyle()
                .setMarginRight("10px")
                .setCursor("pointer");

        UsersModel currentUser = VaadinSession.getCurrent().getAttribute(UsersModel.class);
        String username = currentUser != null ? currentUser.getUsername() : "Guest";

        Image userImage = new Image();
        if (currentUser != null && currentUser.getFoto() != null && !currentUser.getFoto().isEmpty()) {
            userImage.setSrc(currentUser.getFoto());
            userImage.setWidth("50px");
            userImage.setHeight("50px");
        } else {
            userImage.setSrc("images/usericon.png");
            userImage.setWidth("40px");
            userImage.setHeight("40px");
        }
        userImage.getStyle()
                .set("border-radius", "50%")
                .set("object-fit", "cover");

        Span userName = new Span(username);
        userName.addClassName("lora-text");
        userName.getStyle()
                .setFontSize("1.2rem")
                .setMarginLeft("-5px")
                .setFontWeight("bold");

        userLayout.addClickListener(e -> {
            userLayout.getUI().ifPresent(ui -> ui.navigate("profile"));
        });

        userLayout.add(userImage, userName);
        header.add(brandLayout, userLayout);
        return header;
    }

    public static HorizontalLayout headerWithBackButton(String text) {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setPadding(true);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.getStyle()
                .set("background", "white")
                .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)")
                .set("position", "sticky")
                .set("top", "0")
                .set("z-index", "1000");

        HorizontalLayout backLayout = new HorizontalLayout();
        backLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        backLayout.getStyle()
                .setCursor("pointer");

        Icon backBtn = new Icon(VaadinIcon.BACKSPACE_A);
        backBtn.setColor("#16C47F");
        backBtn.setSize("30px");

        backBtn.addClickListener(e -> {
            backBtn.getUI().ifPresent(ui -> ui.getPage().getHistory().back());
        });

        Span textTitle = new Span(text);
        textTitle.addClassName("kavoon-text");
        textTitle.getStyle()
                .setFontSize("24px")
                .setFontWeight("bold")
                .setColor("#16C47F")
                .setMarginLeft("-5px");

        backLayout.add(backBtn, textTitle);
        header.add(backLayout);

        return header;
    }
}
