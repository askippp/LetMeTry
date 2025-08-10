package com.example.application.view;

import com.example.application.dao.UsersDAO;
import com.example.application.model.UsersModel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.sql.SQLException;

@Route("sign-up")
@PageTitle("Sign Up")
public class SignUpPage extends VerticalLayout implements BeforeEnterObserver {

    public SignUpPage() {}

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        UsersModel user = VaadinSession.getCurrent().getAttribute(UsersModel.class);
        if (user != null) {
            if ("admin".equalsIgnoreCase(user.getRole())) {
                event.forwardTo("users");
            } else if ("user".equalsIgnoreCase(user.getRole())) {
                event.forwardTo("dashboard");
            }
        } else {
            buildLayout();
        }
    }

    private void buildLayout() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);
        getStyle().setPaddingTop("40px");

        getStyle()
                .set("background-image", "url('images/background.jpg')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("background-repeat", "no-repeat");

        Image logo = new Image("images/logo.png", "LetMeTry Logo");
        logo.setHeight("90px");

        Span appName = new Span("LetMeTry");
        appName.addClassName("kavoon-text");
        appName.getStyle()
                .setColor("#16C47F")
                .setFontSize("28px");

        HorizontalLayout logoSection = new HorizontalLayout(logo, appName);
        logoSection.setAlignItems(Alignment.CENTER);
        logoSection.setSpacing(true);

        H2 welcomeText = new H2("Selamat Datang di LetMeTry");
        welcomeText.addClassName("lora-text");
        welcomeText.getStyle()
                .setFontWeight("bold")
                .setMarginTop("-20px");

        Span signupPrompt = new Span("Sudah punya akun? Masuk Sekarang");
        signupPrompt.addClassName("lora-text");
        signupPrompt.getStyle()
                .setColor("#888")
                .setFontSize("18px")
                .setMarginTop("-10px");

        VerticalLayout formLayout = new VerticalLayout();
        formLayout.setWidth("450px");
        formLayout.setPadding(true);
        formLayout.setSpacing(true);
        formLayout.setAlignItems(Alignment.STRETCH);
        formLayout.getStyle()
                .setBackgroundColor("white")
                .setBorderRadius("16px")
                .setBoxShadow("0 4px 12px rgba(0, 0, 0, 0.1)")
                .setPaddingTop("10px")
                .setPaddingBottom("20px")
                .setPaddingLeft("30px")
                .setPaddingRight("30px");

        TextField username = new TextField("Username");
        username.setPlaceholder("Masukkan Username Anda");
        username.setRequired(true);
        username.setErrorMessage("Username wajib diisi");
        username.addClassName("lora-text");
        username.getStyle()
                .setFontWeight("bold");

        TextField email = new TextField("Email");
        email.setPlaceholder("Contoh: qwert@gmail.com");
        email.setRequired(true);
        email.setErrorMessage("Email wajib diisi");
        email.addClassName("lora-text");
        email.getStyle()
                .setFontWeight("bold");

        PasswordField password = new PasswordField("Password");
        password.setPlaceholder("Masukkan Password Untuk Akun Anda");
        password.setRequired(true);
        password.setErrorMessage("Password wajib diisi");
        password.addClassName("lora-text");
        password.getStyle()
                .setFontWeight("bold");

        Button daftarBtn = new Button("DAFTAR");
        daftarBtn.addClassNames("inter-text");
        daftarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        daftarBtn.getStyle()
                .setBackgroundColor("#16C47F")
                .setBorderRadius("10px")
                .setPadding("10px 20px")
                .setFontWeight("bold");

        formLayout.add(username, email, password, daftarBtn);

        add(logoSection, welcomeText, signupPrompt, formLayout);

        daftarBtn.addClickListener(buttonClickEvent -> {
            String usrname = username.getValue();
            String mail = email.getValue();
            String pass = password.getValue();

            if(usrname.isEmpty() || mail.isEmpty() || pass.isEmpty()) {
                Notification.show("Semua field wajib diisi!", 3000, Notification.Position.MIDDLE);
                return;
            }

            try {
                UsersDAO dao = new UsersDAO();

                if(!dao.checkEmail(mail)) {
                    Notification.show("Email sudah terdaftar!", 3000, Notification.Position.MIDDLE);
                    return;
                }

                UsersModel users = new UsersModel();
                users.setUsername(usrname);
                users.setEmail(mail);
                users.setPassword(pass);

                dao.addUsers(users);
                Notification.show("Registrasi berhasil!", 3000, Notification.Position.TOP_CENTER);
                getUI().ifPresent(
                        ui -> ui.navigate("login")
                );
            } catch (SQLException e) {
                e.printStackTrace();
                Notification.show("Terjadi kesalahan: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });
    }
}
