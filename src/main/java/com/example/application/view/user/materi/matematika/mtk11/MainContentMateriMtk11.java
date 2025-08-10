package com.example.application.view.user.materi.matematika.mtk11;

import com.example.application.components.Header;
import com.example.application.components.SidebarMateri;
import com.example.application.listeners.MateriClickListener;
import com.example.application.model.UsersModel;
import com.example.application.view.user.materi.matematika.ModelMateriMtk;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;

import static com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER;

@Route("materi-mtk-11")
@PageTitle("Materi - Matematika - 11")
public class MainContentMateriMtk11 extends VerticalLayout implements BeforeEnterObserver, MateriClickListener {

    private VerticalLayout mainContent;

    public MainContentMateriMtk11() {}

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        UsersModel user = VaadinSession.getCurrent().getAttribute(UsersModel.class);
        if (user == null) {
            event.forwardTo("login");
            Notification.show("Kamu Harus Login Terlebih dahulu!", 2000, TOP_CENTER);
        } else {
            buildLayout();
        }
    }

    private void buildLayout() {
        setPadding(false);
        setMargin(false);
        setSpacing(false);
        setSizeFull();

        HorizontalLayout header = Header.headerWithBackButton("Matematika Kelas 11");

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();
        mainLayout.setPadding(false);
        mainLayout.setMargin(false);
        mainLayout.setSpacing(false);

        VerticalLayout sidebar = SidebarMateri.createSidebarMateri(
                1, "11", "mtk.png", this
        );
        sidebar.setHeightFull();
        sidebar.getStyle()
                .setOverflow(Style.Overflow.AUTO);

        VerticalLayout mainContent = createMainContent();
        mainContent.setHeightFull();
        mainContent.getStyle()
                .setOverflow(Style.Overflow.AUTO);

        mainLayout.add(sidebar, mainContent);
        mainLayout.setFlexGrow(0, sidebar);
        mainLayout.setFlexGrow(1, mainContent);

        add(header, mainLayout);
        setFlexGrow(0, header);
        setFlexGrow(1, mainLayout);

        onMateriClick("Fungsi");
    }

    private VerticalLayout createMainContent() {
        mainContent = new VerticalLayout();
        mainContent.setSizeFull();
        mainContent.setPadding(true);
        mainContent.setSpacing(true);
        mainContent.getStyle()
                .set("background-image", "url('images/background.jpg')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("background-repeat", "no-repeat");

        List<ModelMateriMtk> daftarMateri = DaftarMateriMtk11.getDaftarMateriMtk11();

        for (ModelMateriMtk materiMtk : daftarMateri) {
            mainContent.add(createMateriCard(materiMtk));
        }

        return mainContent;
    }

    private VerticalLayout createMateriCard(ModelMateriMtk materiMtk) {
        VerticalLayout card = new VerticalLayout();
        card.setWidthFull();

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidthFull();
        topLayout.setSpacing(false);
        topLayout.setAlignItems(Alignment.CENTER);
        topLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        H2 judul = new H2(materiMtk.getJudul());
        judul.addClassName("lora-text");
        judul.getStyle()
                .setColor("#118B50")
                .setMarginBottom("10px");

        Button btnLatsol = new Button("LATIHAN SOAL");
        btnLatsol.addClassNames("inter-text");
        btnLatsol.getStyle()
                .setColor("#FFFFFF")
                .setBackgroundColor("#16C47F")
                .setBorderRadius("10px")
                .setBorderBottom("2px solid #0e9e66")
                .setPadding("10px 20px")
                .setFontWeight("bold");

        btnLatsol.addClickListener(buttonClickEvent -> {
            try {
                if (materiMtk.getIdMateri() > 0) {
                    UI.getCurrent().navigate("latsol-mtk-11/" + materiMtk.getIdMateri());
                } else {
                    Notification.show("Materi tidak valid", 3000, Notification.Position.MIDDLE);
                }
            } catch (Exception e) {
                Notification.show("Gagal membuka latihan soal", 3000, Notification.Position.MIDDLE);
                e.printStackTrace();
            }
        });

        topLayout.add(judul, btnLatsol);

        H3 pengertianTitle = new H3("Pengertian");
        pengertianTitle.addClassName("lora-text");
        pengertianTitle.getStyle()
                .setColor("#118B50")
                .setMarginBottom("-10px");

        Paragraph pengertian = new Paragraph(materiMtk.getPengertian());
        pengertian.addClassName("lora-text");
        pengertian.getStyle()
                .setFontSize("16px")
                .setTextAlign(Style.TextAlign.LEFT);

        H3 rumusTitle = new H3("Rumus");
        rumusTitle.addClassName("lora-text");
        rumusTitle.getStyle()
                .setColor("#118B50")
                .setMarginBottom("-10px");

        VerticalLayout listRumus = new VerticalLayout();
        listRumus.addClassName("lora-text");
        listRumus.setPadding(false);
        listRumus.setSpacing(false);
        listRumus.setMargin(false);


        for (String rumus : materiMtk.getRumus()) {
            listRumus.add(new Paragraph(rumus));
        }

        H3 contohTitle = new H3("Contoh:");
        contohTitle.addClassName("lora-text");
        contohTitle.getStyle()
                .setColor("#118B50")
                .setMarginBottom("-10px");

        VerticalLayout listContoh = new VerticalLayout();
        listContoh.addClassName("lora-text");
        listContoh.setPadding(false);
        listContoh.setSpacing(false);
        listContoh.setMargin(false);

        for (String contoh : materiMtk.getContoh()) {
            listContoh.add(new Paragraph(contoh));
        }

        H3 penjelasanTitle = new H3("Penjelasan");
        penjelasanTitle.addClassName("lora-text");
        penjelasanTitle.getStyle()
                .setColor("#118B50")
                .setMarginBottom("-10px");

        VerticalLayout penjelasan = new VerticalLayout();
        penjelasan.addClassName("lora-text");
        penjelasan.setPadding(false);
        penjelasan.setSpacing(false);
        penjelasan.setMargin(false);

        for (String p : materiMtk.getPenjelasan()) {
            penjelasan.add(new Paragraph(p));
        }

        card.add(topLayout, pengertianTitle, pengertian, rumusTitle, listRumus, contohTitle, listContoh, penjelasanTitle, penjelasan);
        return card;
    }

    @Override
    public void onMateriClick(String judul) {
        mainContent.removeAll();

        ModelMateriMtk materiTerpilih = DaftarMateriMtk11.getDaftarMateriMtk11().stream()
                .filter(m -> m.getJudul().equalsIgnoreCase(judul))
                .findFirst()
                .orElse(null);

        if (materiTerpilih != null) {
            mainContent.add(createMateriCard(materiTerpilih));
        } else {
            mainContent.add(new Paragraph("Materi tidak ditemukan!"));
        }
    }
}
