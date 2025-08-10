package com.example.application.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("Landing Page")
public class LandingPage extends VerticalLayout {

    public LandingPage() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        header();
        mainContent();
        bottomContent();
    }

    private void header() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setPadding(true);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setAlignItems(Alignment.CENTER);
        header.getStyle()
                .setBackgroundColor("white")
                .set("position", "sticky")
                .set("top", "0")
                .set("z-index", "1000")
                .setBoxShadow("0 2px 8px rgba(0,0,0,0.1)");

        HorizontalLayout logoSection = new HorizontalLayout();
        logoSection.setAlignItems(Alignment.CENTER);

        Div logoPlaceholder = new Div();
        logoPlaceholder.getStyle()
                .setWidth("60px")
                .setHeight("60px")
                .set("background-image", "url('images/logo.png')")
                .set("background-size", "contain")
                .set("background-repeat", "no-repeat")
                .set("background-position", "center");

        H3 brandName = new H3("LetMeTry");
        brandName.addClassName("kavoon-text");
        brandName.getStyle()
                .setMargin("0")
                .setColor("#333")
                .setFontSize("1.5rem");

        logoSection.add(logoPlaceholder, brandName);

        HorizontalLayout navButtons = new HorizontalLayout();
        navButtons.setSpacing(true);

        Button masukBtn = new Button("MASUK");
        masukBtn.addClassNames("inter-text");
        masukBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        masukBtn.getStyle()
                .setBorder("2px solid #16C47F")
                .setColor("#333")
                .setBorderRadius("10px")
                .setPadding("8px 20px")
                .setFontWeight("bold")
                .setZIndex(5);

        masukBtn.addClickListener(buttonClickEvent -> {
            UI.getCurrent().navigate("login");
        });

        Button daftarBtn = new Button("DAFTAR");
        daftarBtn.addClassNames("inter-text");
        daftarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        daftarBtn.getStyle()
                .setBackgroundColor("#00b386")
                .setBorderRadius("10px")
                .setPadding("8px 20px")
                .setFontWeight("bold")
                .setZIndex(5);

        daftarBtn.addClickListener(buttonClickEvent ->{
            UI.getCurrent().navigate("sign-up");
        });

        navButtons.add(masukBtn, daftarBtn);

        header.add(logoSection, navButtons);
        add(header);
    }

    private void mainContent() {
        HorizontalLayout mainContent = new HorizontalLayout();
        mainContent.setSizeFull();
        mainContent.setPadding(true);
        mainContent.setSpacing(true);
        mainContent.setAlignItems(Alignment.CENTER);
        mainContent.setJustifyContentMode(JustifyContentMode.CENTER);

        mainContent.getStyle()
                .set("background-image", "url('images/background-landing-page.jpg')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("background-repeat", "no-repeat")
                .setPosition(Style.Position.RELATIVE)
                .setMinHeight("100%");

        VerticalLayout leftContent = new VerticalLayout();
        leftContent.setSpacing(true);
        leftContent.setPadding(false);
        leftContent.setWidth("50%");

        VerticalLayout headingLayout = new VerticalLayout();
        headingLayout.setSpacing(false);
        headingLayout.setPadding(false);

        H1 mainText1 = new H1("Raih Kampus");
        mainText1.addClassName("lora-text");
        mainText1.getStyle()
                .setMarginLeft("30px")
                .setMarginRight("20px")
                .setFontSize("3rem")
                .setFontWeight("bold")
                .setColor("#333")
                .setLineHeight("1.2");

        H1 mainText2 = new H1("Idamanmu");
        mainText2.addClassName("lora-text");
        mainText2.getStyle()
                .setMarginLeft("30px")
                .setMarginRight("20px")
                .setFontSize("3rem")
                .setFontWeight("bold")
                .setColor("#333")
                .setLineHeight("1.2");

        HorizontalLayout brandLine = new HorizontalLayout();
        brandLine.setSpacing(false);
        brandLine.setPadding(false);
        brandLine.setAlignItems(Alignment.BASELINE);

        H1 mainText3 = new H1("Bersama ");
        mainText3.addClassName("lora-text");
        mainText3.getStyle()
                .setMarginLeft("30px")
                .setMarginRight("15px")
                .setFontSize("3rem")
                .setFontWeight("bold")
                .setColor("#333")
                .setLineHeight("1.2");

        H1 brandText = new H1(" LetMeTry.");
        brandText.addClassName("kavoon-text");
        brandText.getStyle()
                .setMargin("0")
                .setFontSize("3rem")
                .setColor("#00b386")
                .setLineHeight("1.2");

        brandLine.add(mainText3, brandText);

        headingLayout.add(mainText1, mainText2, brandLine);

        Paragraph subText1 = new Paragraph("Taklukan TKA 2025 dengan persiapan terbaik\n bersama LetMeTry.");
        subText1.addClassName("lora-text");
        subText1.getStyle()
                .setColor("#666")
                .setFontSize("1.5rem")
                .setMarginTop("20px")
                .setMarginBottom("20px")
                .setMarginLeft("30px");

        HorizontalLayout actionButtons = new HorizontalLayout();
        actionButtons.setSpacing(true);
        actionButtons.setMargin(false);

        Button mulaiTOBtn = new Button("Mulai Try Out");
        mulaiTOBtn.addClassNames("inter-text");
        mulaiTOBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        mulaiTOBtn.getStyle()
                .setBackgroundColor("#00b386")
                .setBorderRadius("10px")
                .setFontSize("1.1rem")
                .setPadding("15px")
                .setFontWeight("bold")
                .setMarginLeft("30px")
                .setFontWeight("bold");

        mulaiTOBtn.addClickListener(buttonClickEvent ->
                UI.getCurrent().navigate("sign-up")
        );

        Button punyaAkunBtn = new Button("Sudah Punya Akun");
        punyaAkunBtn.addClassNames("inter-text");
        punyaAkunBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        punyaAkunBtn.getStyle().setBorder("2px solid #16C47F")
                .setColor("#333")
                .setFontSize("1.1rem")
                .setBorderRadius("10px")
                .setPadding("15px")
                .setFontWeight("bold")
                .setBackgroundColor("white");

        punyaAkunBtn.addClickListener(buttonClickEvent ->
                UI.getCurrent().navigate("login")
        );

        actionButtons.add(mulaiTOBtn, punyaAkunBtn);

        leftContent.add(headingLayout, subText1, actionButtons);

        VerticalLayout rightContent = new VerticalLayout();
        rightContent.setWidth("50%");
        rightContent.setAlignItems(Alignment.CENTER);
        rightContent.setJustifyContentMode(JustifyContentMode.CENTER);

        Div mascotPlaceholder = new Div();
        mascotPlaceholder.getStyle()
                .setWidth("400px")
                .setHeight("400px")
                .setBackground("#D3F1DF")
                .setBorderRadius("50%")
                .setDisplay(Style.Display.FLEX)
                .setAlignItems(Style.AlignItems.CENTER)
                .setJustifyContent(Style.JustifyContent.CENTER)
                .setPosition(Style.Position.RELATIVE)
                .setOverflow(Style.Overflow.VISIBLE)
                .set("z-index", "1");

        Image mascotImage = new Image();
        mascotImage.setSrc("images/mascot.png");
        mascotImage.setAlt("Maskot LetMeTry");
        mascotImage.getStyle()
                .setWidth("650px")
                .setHeight("650px")
                .set("object-fit", "contain")
                .setPosition(Style.Position.ABSOLUTE)
                .set("top", "-155px")
                .set("left", "-190px");

        mascotPlaceholder.add(mascotImage);
        rightContent.add(mascotPlaceholder);

        mainContent.add(leftContent, rightContent);
        add(mainContent);

        setFlexGrow(1, mainContent);
    }

    private void bottomContent() {
        VerticalLayout bottomContent = new VerticalLayout();
        bottomContent.setSizeFull();
        bottomContent.setPadding(true);
        bottomContent.setSpacing(true);
        bottomContent.setAlignItems(Alignment.CENTER);

        bottomContent.getStyle()
                .set("background-image", "url('images/background.jpg')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("background-repeat", "no-repeat");

        Paragraph subTitle = new Paragraph("Fitur Try Out LetMeTry.");
        subTitle.addClassName("lora-text");
        subTitle.getStyle()
                .setColor("#00b386")
                .setFontSize("1.6rem")
                .setMarginTop("50px");

        H2 title = new H2("Mengapa LetMeTry?");
        title.addClassName("lora-text");
        title.getStyle()
                .setColor("#333")
                .setFontSize("2.2rem")
                .setMarginTop("-20px");

        HorizontalLayout cardsLayout = new HorizontalLayout();
        cardsLayout.setWidthFull();
        cardsLayout.setHeight("600px");
        cardsLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        cardsLayout.setSpacing(true);
        cardsLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        cardsLayout.getStyle()
                .setPaddingRight("30px")
                .setPaddingLeft("30px")
                .setPaddingBottom("50px");

        VerticalLayout card1 = new VerticalLayout();
        card1.addClassName("lora-text");
        card1.setPadding(true);
        card1.setSpacing(true);
        card1.setWidthFull();
        card1.setHeightFull();
        card1.getStyle()
                .setMarginTop("15px")
                .setBackgroundColor("white")
                .setPaddingTop("15px")
                .setPaddingBottom("15px")
                .setPaddingRight("20px")
                .setPaddingLeft("20px")
                .setBorderRadius("30px")
                .setBoxShadow("0 0 5px rgba(0,0,0,0.3)");

        Paragraph titleText1 = new Paragraph("Hasil Try Out Lengkap");
        titleText1.getStyle()
                .setColor("#333")
                .setFontWeight("bold")
                .setFontSize("1.6rem")
                .setMarginTop("0px")
                .setMarginBottom("-20px");

        Paragraph descText1 = new Paragraph(
                "Penilaian dalam LetMeTry menggunakan sistem penilaian IRT yang sudah disesuaikan dengan TKA, sehingga hasil yang didapatkan mirip ketika mengikuti TKA sebenarnya."
        );

        descText1.getStyle()
                .setColor("#666")
                .setMarginBottom("0px");

        HorizontalLayout point1 = new HorizontalLayout();
        point1.setWidthFull();
        point1.getStyle()
                .setBackgroundColor("#D3F1DF")
                .setBoxShadow("0 1px 1px rgba(17, 139, 80, 1.0)")
                .setPadding("23px")
                .setBorderRadius("30px")
                .setAlignItems(Style.AlignItems.CENTER);

        Image icon1 = new Image("images/scoreicon.png", "Icon Skor");
        icon1.getStyle().setHeight("50px");
        H3 text1 = new H3("Score");
        text1.getStyle().setFontSize("1.3rem").setFontWeight("bold");
        point1.add(icon1, text1);

        HorizontalLayout point2 = new HorizontalLayout();
        point2.setWidthFull();
        point2.getStyle()
                .setBackgroundColor("#E8F9FF")
                .setBoxShadow("0 1px 1px rgba(9, 63, 180, 0.7)")
                .setPadding("23px")
                .setBorderRadius("30px")
                .setAlignItems(Style.AlignItems.CENTER);

        Image icon2 = new Image("images/historyicon.png", "Icon Histori");
        icon2.getStyle().setHeight("50px");
        H3 text2 = new H3("History");
        text2.getStyle().setFontSize("1.3rem").setFontWeight("bold");
        point2.add(icon2, text2);

        HorizontalLayout point3 = new HorizontalLayout();
        point3.setWidthFull();
        point3.getStyle()
                .setBackgroundColor("#FFE6E1")
                .setBoxShadow("0 1px 1px rgba(255, 63, 51, 0.8)")
                .setPadding("23px")
                .setBorderRadius("30px")
                .setAlignItems(Style.AlignItems.CENTER);

        Image icon3 = new Image("images/notesicon.png", "Icon Catatan");
        icon3.getStyle().setHeight("50px");
        H3 text3 = new H3("Pembahasan Lengkap");
        text3.getStyle().setFontSize("1.3rem").setFontWeight("bold");
        point3.add(icon3, text3);

        card1.add(titleText1, descText1, point1, point2, point3);

        VerticalLayout middleCards = new VerticalLayout();
        middleCards.addClassName("lora-text");
        middleCards.setWidthFull();
        middleCards.setHeightFull();
        middleCards.setSpacing(true);

        VerticalLayout card2 = new VerticalLayout();
        card2.setWidthFull();
        card2.setHeight("250px");
        card2.getStyle()
                .setBackgroundColor("white")
                .setBorderRadius("30px")
                .setBoxShadow("0 0 5px rgba(0,0,0,0.3)");

        Paragraph titleText2 = new Paragraph("Bidang TKA per Mata Pelajaran");
        titleText2.getStyle()
                .setColor("#333")
                .setFontWeight("bold")
                .setFontSize("1.6rem")
                .setMarginTop("0px")
                .setMarginBottom("-20px");

        Paragraph descText2 = new Paragraph(
                "Lihat performamu di tiap mata pelajaran seperti Matematika, Bahasa Indonesia, dan Bahasa Inggris. Temukan mata pelajaran yang paling kuat dan yang masih perlu ditingkatkan."
        );
        descText2.getStyle().setColor("#666").setMarginBottom("0px");

        card2.add(titleText2, descText2);

        VerticalLayout card3 = new VerticalLayout();
        card3.setWidthFull();
        card3.setHeight("250px");
        card3.getStyle()
                .setBackgroundColor("white")
                .setMarginTop("30px")
                .setMarginBottom("-30px")
                .setBorderRadius("30px")
                .setBoxShadow("0 0 5px rgba(0,0,0,0.3)");

        Paragraph titleText3 = new Paragraph("Pembahasan Lengkap & Terstruktur");
        titleText3.getStyle()
                .setColor("#333")
                .setFontWeight("bold")
                .setFontSize("1.6rem")
                .setMarginTop("0px")
                .setMarginBottom("-20px");

        Paragraph descText3 = new Paragraph(
                "Semua soal dilengkapi pembahasan detail yang sesuai dengan kisi-kisi resmi TKA. Belajar jadi lebih fokus dan efisien."
        );
        descText3.getStyle().setColor("#666").setMarginBottom("0px");

        card3.add(titleText3, descText3);
        middleCards.add(card2, card3);

        VerticalLayout card4 = new VerticalLayout();
        card4.addClassName("lora-text");
        card4.setPadding(true);
        card4.setSpacing(true);
        card4.setWidthFull();
        card4.setHeightFull();
        card4.getStyle()
                .setMarginTop("15px")
                .setBackgroundColor("white")
                .setPaddingTop("15px")
                .setPaddingBottom("15px")
                .setPaddingRight("20px")
                .setPaddingLeft("20px")
                .setBorderRadius("30px")
                .setBoxShadow("0 0 5px rgba(0,0,0,0.3)");

        Paragraph titleText4 = new Paragraph("Level Soal");
        titleText4.getStyle()
                .setColor("#333")
                .setFontWeight("bold")
                .setFontSize("1.6rem")
                .setMarginTop("0px")
                .setMarginBottom("-20px");

        Paragraph descText4 = new Paragraph(
                "Soal-soal Try Out dikelompokkan berdasarkan tingkat kesulitan: mudah, sedang, dan sulit. Ketahui performamu di tiap level untuk belajar lebih efektif."
        );
        descText4.getStyle().setColor("#666").setMarginBottom("0px");

        HorizontalLayout point4 = new HorizontalLayout();
        point4.setWidthFull();
        point4.getStyle()
                .setBackgroundColor("#D3F1DF")
                .setBoxShadow("0 1px 1px rgba(17, 139, 80, 1.0)")
                .setPadding("10px")
                .setBorderRadius("30px")
                .setAlignItems(Style.AlignItems.CENTER);

        Image icon4 = new Image("images/ceklisicon.png", "Icon Ceklis");
        icon4.getStyle().setMarginLeft("15px").setHeight("40px");

        VerticalLayout vl1 = new VerticalLayout();
        vl1.setSpacing(false);
        vl1.setPadding(false);
        H3 text4 = new H3("Easy");
        text4.getStyle().setFontSize("1.3rem").setFontWeight("bold");
        Paragraph descPoin4 = new Paragraph("Kamu menguasai soal-soal dasar dengan sangat baik. Pertahankan!");
        descPoin4.getStyle().setColor("#666").setFontSize("0.7rem");
        vl1.add(text4, descPoin4);
        point4.add(icon4, vl1);

        HorizontalLayout point5 = new HorizontalLayout();
        point5.setWidthFull();
        point5.getStyle()
                .setBackgroundColor("#FDFFB8")
                .setBoxShadow("0 1px 1px rgba(255, 203, 97, 0.8)")
                .setPadding("10px")
                .setBorderRadius("30px")
                .setAlignItems(Style.AlignItems.CENTER);

        Image icon5 = new Image("images/warningicon.png", "Icon Warning");
        icon5.getStyle().setMarginLeft("15px").setHeight("40px");

        VerticalLayout vl2 = new VerticalLayout();
        vl2.setSpacing(false);
        vl2.setPadding(false);
        H3 text5 = new H3("Medium");
        text5.getStyle().setFontSize("1.3rem").setFontWeight("bold");
        Paragraph descPoin5 = new Paragraph("Beberapa soal tingkat sedang masih bisa ditingkatkan. Perlu latihan rutin.");
        descPoin5.getStyle().setColor("#666").setFontSize("0.7rem");
        vl2.add(text5, descPoin5);
        point5.add(icon5, vl2);

        HorizontalLayout point6 = new HorizontalLayout();
        point6.setWidthFull();
        point6.getStyle()
                .setBackgroundColor("#FFD8D8")
                .setBoxShadow("0 1px 1px rgba(255, 63, 51, 0.8)")
                .setPadding("10px")
                .setBorderRadius("30px")
                .setAlignItems(Style.AlignItems.CENTER);

        Image icon6 = new Image("images/apicon.png", "Icon Api");
        icon6.getStyle().setMarginLeft("15px").setHeight("40px");

        VerticalLayout vl3 = new VerticalLayout();
        vl3.setSpacing(false);
        vl3.setPadding(false);
        H3 text6 = new H3("Hard");
        text6.getStyle().setFontSize("1.3rem").setFontWeight("bold");
        Paragraph descPoin6 = new Paragraph("Soal sulit masih menjadi tantangan. Fokus di bagian ini agar skor maksimal.");
        descPoin6.getStyle().setColor("#666").setFontSize("0.7rem");
        vl3.add(text6, descPoin6);
        point6.add(icon6, vl3);

        card4.add(titleText4, descText4, point4, point5, point6);

        cardsLayout.add(card1, middleCards, card4);
        bottomContent.add(subTitle, title, cardsLayout);
        add(bottomContent);
    }
}