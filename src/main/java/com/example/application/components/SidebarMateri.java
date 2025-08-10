package com.example.application.components;

import com.example.application.dao.MateriDAO;
import com.example.application.listeners.MateriClickListener;
import com.example.application.model.MateriModel;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;

import java.util.List;

public class SidebarMateri {

    public static VerticalLayout createSidebarMateri(int idMapel, String kelas, String gambar, MateriClickListener listener) {
        VerticalLayout sidebar = new VerticalLayout();
        sidebar.setWidth("350px");
        sidebar.setHeightFull();
        sidebar.setPadding(true);
        sidebar.setSpacing(false);
        sidebar.getStyle()
                .setOverflow(Style.Overflow.AUTO)
                .setBackgroundColor("#D3F1DF")
                .setBorderRight("1px solid #e2e8f0")
                .setPadding("12px");

        MateriDAO dao = new MateriDAO();
        List<MateriModel> daftarMateri = dao.getAllMateriMtkSepuluh(idMapel, kelas);

        for (MateriModel materi : daftarMateri) {
            HorizontalLayout btn = createMateriButton(materi.getNamaMateri(), createMateriIcon(gambar), listener);
            sidebar.add(btn);
        }

        return sidebar;
    }

    private static Image createMateriIcon(String imageName) {
        Image mathSymbol1 = new Image();
        mathSymbol1.setSrc("images/" + imageName);
        mathSymbol1.setAlt("materi Icon");
        mathSymbol1.getStyle()
                .setJustifyContent(Style.JustifyContent.LEFT)
                .setWidth("45px")
                .setHeight("45px")
                .setMarginRight("12px")
                .setFlexShrink("0"); // Mencegah icon mengecil

        return mathSymbol1;
    }

    private static HorizontalLayout createMateriButton(String materi, Image icon, MateriClickListener listener) {
        HorizontalLayout btnMateri = new HorizontalLayout();
        btnMateri.setSpacing(false);
        btnMateri.setAlignItems(FlexComponent.Alignment.CENTER);
        btnMateri.setPadding(true);
        btnMateri.setWidthFull();
        btnMateri.getStyle()
                .setPadding("8px 12px")
                .setBackgroundColor("#FFF3F3")
                .setBorderRadius("10px")
                .setCursor("pointer")
                .setMarginBottom("8px");

        Span namaMateri = new Span(materi);
        namaMateri.addClassName("lora-text");
        namaMateri.setWidthFull(); // penting
        namaMateri.getStyle()
                .set("white-space", "nowrap")
                .set("overflow", "hidden")
                .set("text-overflow", "ellipsis")
                .set("font-size", "1rem")
                .setFontWeight("bold")
                .set("color", "#118B50")
                .set("min-width", "0")
                .set("flex-grow", "1");

        btnMateri.add(icon, namaMateri);

        btnMateri.getElement().setAttribute("title", materi);

        btnMateri.addClickListener(e -> {
            listener.onMateriClick(materi);
        });

        return btnMateri;
    }
}