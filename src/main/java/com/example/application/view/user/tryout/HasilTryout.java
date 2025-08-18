package com.example.application.view.user.tryout;

import com.example.application.components.Header;
import com.example.application.dao.TryOutEasyDAO;
import com.example.application.model.TryOutEasyModel;
import com.example.application.model.UsersModel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;

import java.sql.SQLException;

import static com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER;

@Route("hasil-tryout/:idToEasy?")  // Mengubah route untuk menerima parameter
@PageTitle("Hasil TryOut")
public class HasilTryout extends VerticalLayout implements BeforeEnterObserver, HasUrlParameter<String> {

    private int idToEasy;

    public HasilTryout() {}

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        UsersModel user = VaadinSession.getCurrent().getAttribute(UsersModel.class);
        if (user == null) {
            event.forwardTo("login");
            Notification.show("Kamu Harus Login Terlebih dahulu!", 2000, TOP_CENTER);
        } else if (user.getRole().equalsIgnoreCase("admin")) {
            event.forwardTo("users");
            Notification.show("Anda adalah Admin!", 2000, TOP_CENTER);
        } else if (user.getStatus().equalsIgnoreCase("Tidak Diterima")) {
            event.forwardTo("");
            Notification.show("Akun anda tidak diterima!", 2000, TOP_CENTER);
        } else {
            buildLayout();
        }
    }

    private void buildLayout() {
        setPadding(false);
        setMargin(false);
        setSpacing(false);
        setSizeFull();

        HorizontalLayout header = Header.headerWithBackButton("Back");

        VerticalLayout mainContent = new VerticalLayout();
        mainContent.setHeightFull();
        mainContent.setSizeFull();
        mainContent.setAlignItems(FlexComponent.Alignment.CENTER);
        mainContent.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        mainContent.getStyle()
                .set("background-color", "#E8F5E8")
                .set("min-height", "100vh");

        H3 title = new H3("Try Out LetMeTry");

        TryOutEasyDAO dao = new TryOutEasyDAO();
        TryOutEasyModel hasil = null;
        try {
            hasil = dao.getHasilTryOut(idToEasy);
        } catch (SQLException e) {
            e.printStackTrace();
            Notification.show("Gagal mengambil data hasil tryout!", 2000, TOP_CENTER);
            return;
        }

        if (hasil == null) {
            Notification.show("Data hasil tryout tidak ditemukan!", 2000, TOP_CENTER);
            return;
        }

        // Menggunakan data yang benar dari hasil tryout
        int nilai = hasil.getNilai();
        int benar = hasil.getJumlahBenar();
        int salah = hasil.getJumlahSalah();
        int kosong = hasil.getJumlahKosong();

        // Nilai besar
        H1 nilaiText = new H1(String.valueOf(nilai));
        H4 labelNilai = new H4("NILAI RATA-RATA");

        // Kotak statistik
        Button benarBox = new Button("Benar: " + benar);
        benarBox.getStyle()
                .set("background-color", "#2E7D32")
                .set("color", "white");

        Button salahBox = new Button("Salah: " + salah);
        salahBox.getStyle()
                .set("background-color", "#FFCDD2")
                .set("color", "black");

        Button kosongBox = new Button("Kosong: " + kosong);
        kosongBox.getStyle()
                .set("background-color", "#4CAF50")
                .set("color", "white");

        HorizontalLayout statsLayout = new HorizontalLayout(benarBox, salahBox, kosongBox);
        statsLayout.setSpacing(true);
        statsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // Tombol history
        Button goHistory = new Button("Go to History", e -> {
            getUI().ifPresent(ui -> ui.navigate("my-history"));
        });
        goHistory.getStyle()
                .set("background-color", "#00C853")
                .set("color", "white")
                .set("border-radius", "20px");

        mainContent.add(title, nilaiText, labelNilai, statsLayout, goHistory);

        add(header, mainContent);
        setFlexGrow(0, header);
        setFlexGrow(1, mainContent);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        if (s != null && !s.isEmpty()) {
            try {
                this.idToEasy = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                beforeEvent.forwardTo("dashboard");
                Notification.show("Parameter tidak valid!", 2000, TOP_CENTER);
            }
        }
    }
}