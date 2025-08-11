package com.example.application.view.user.tryout.medium;

import com.example.application.components.Header;
import com.example.application.dao.TryOutMediumDAO;
import com.example.application.model.TryOutMediumModel;
import com.example.application.model.UsersModel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
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

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER;

@Route("tryout-level2/inggris")
@PageTitle("TryOut - Inggris")
public class InggrisMedium extends VerticalLayout implements BeforeEnterObserver {

    private VerticalLayout mainContent;
    private int currentNoSoal = 1;
    private List<TryOutMediumModel> model;

    public InggrisMedium() {}

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        UsersModel user = VaadinSession.getCurrent().getAttribute(UsersModel.class);
        if (user == null) {
            event.forwardTo("login");
            Notification.show("Kamu Harus Login Terlebih dahulu!", 2000, TOP_CENTER);
        } else if (user.getRole().equalsIgnoreCase("admin")) {
            event.forwardTo("users");
            Notification.show("Anda adalah Admin!", 2000, TOP_CENTER);
        } else if(user.getPoint() < 200) {
            event.forwardTo("dashboard");
            Notification.show("Point anda kurang! Butuh 500 point untuk ke level ini!", 2000, TOP_CENTER);
        } else if (user.getStatus().equalsIgnoreCase("Tidak Diterima")) {
            event.forwardTo("");
            Notification.show("Akun anda tidak diterima!", 2000, TOP_CENTER);
        } else {
            try {
                int idMapel = 2;
                model = new TryOutMediumDAO().getTryOutMediumByIdMapel(idMapel);

                buildLayout();
            } catch (SQLException e) {
                e.printStackTrace();
                Notification.show("Gagal mengambil data soal.", 3000, TOP_CENTER);
            }
        }
    }

    private void buildLayout() {
        setPadding(false);
        setMargin(false);
        setSpacing(false);
        setSizeFull();

        HorizontalLayout header = Header.headerWithLogo();

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();
        mainLayout.setPadding(false);
        mainLayout.setMargin(false);
        mainLayout.setSpacing(false);

        mainContent = new VerticalLayout();
        mainContent.setHeightFull();
        mainContent.setSizeFull();
        mainContent.setPadding(true);
        mainContent.setSpacing(true);
        mainContent.getStyle()
                .setOverflow(Style.Overflow.AUTO)
                .set("background-image", "url('images/background.jpg')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("background-repeat", "no-repeat");

        H2 sectionTitle = new H2("TRYOUT INGGRIS");
        sectionTitle.addClassName("lora-text");
        sectionTitle.getStyle()
                .setFontWeight("bold")
                .setColor("#118B50")
                .setMargin("0 0 20px 0");
        mainContent.add(sectionTitle);

        displayQuestion(currentNoSoal);

        mainLayout.add(mainContent);

        add(header, mainLayout);
        setFlexGrow(0, header);
        setFlexGrow(1, mainLayout);
    }

    private void displayQuestion(int questionNumber) {
        if (mainContent.getComponentCount() > 1) {
            mainContent.remove(mainContent.getComponentAt(1));
        }

        List<TryOutMediumModel> currentQuestion = model.stream()
                .filter(q -> q.getNoSoal() == questionNumber)
                .toList();

        if (currentQuestion.isEmpty()) {
            mainContent.add(new Paragraph("Soal tidak ditemukan"));
            return;
        }

        Div mainContainer = new Div();
        mainContainer.getStyle()
                .setBackgroundColor("white")
                .setBorderRadius("20px")
                .setPadding("30px")
                .setBoxShadow("0 4px 15px rgba(0, 0, 0, 0.1)")
                .setPosition(Style.Position.RELATIVE);

        HorizontalLayout contentLayout = new HorizontalLayout();
        contentLayout.setWidthFull();
        contentLayout.setSpacing(true);
        contentLayout.setPadding(false);
        contentLayout.setAlignItems(Alignment.START);

        VerticalLayout leftContainer = new VerticalLayout();
        leftContainer.setSpacing(true);
        leftContainer.setPadding(false);
        leftContainer.getStyle().setFlexGrow("1");

        H3 questionTitle = new H3("Soal NO." + questionNumber);
        questionTitle.addClassName("lora-text");
        questionTitle.getStyle()
                .setFontWeight("bold")
                .setColor("#118B50")
                .setFontSize("24px")
                .setMargin("0 0 20px 0");

        Paragraph questionText = new Paragraph(currentQuestion.getFirst().getPertanyaan());
        questionText.addClassName("lora-text");
        questionText.getStyle()
                .setFontWeight("normal")
                .setFontSize("18px")
                .setColor("#333")
                .setMargin("0 0 30px 0")
                .setLineHeight("1.6");

        VerticalLayout answersLayout = new VerticalLayout();
        answersLayout.setSpacing(false);
        answersLayout.setPadding(false);
        answersLayout.setWidthFull();

        Map<String, String> sortedAnswers = currentQuestion.stream()
                .collect(Collectors.toMap(
                        TryOutMediumModel::getOpsi,
                        TryOutMediumModel::getTextJawaban,
                        (a, b) -> a,
                        java.util.TreeMap::new
                ));

        sortedAnswers.forEach((option, answer) -> {
            Button answerButton = new Button();
            answerButton.getStyle()
                    .setBackgroundColor("#C6ECC9")
                    .setColor("#333")
                    .setBorder("none")
                    .setBorderRadius("10px")
                    .setPadding("15px 20px")
                    .setMargin("0 0 15px 0")
                    .setCursor("pointer")
                    .setWidth("auto")
                    .setMinWidth("120px")
                    .setFontWeight("bold")
                    .setFontSize("16px")
                    .setTextAlign(Style.TextAlign.LEFT);

            answerButton.setText(option + ". " + answer);
            answerButton.addClassName("lora-text");

            answerButton.addClickListener(e -> {
                System.out.println("Jawaban dipilih: " + option);
            });

            answersLayout.add(answerButton);
        });

        leftContainer.add(questionTitle, questionText, answersLayout);

        VerticalLayout rightContainer = new VerticalLayout();
        rightContainer.setWidth("250px");
        rightContainer.setHeightFull();
        rightContainer.setSpacing(false);
        rightContainer.setPadding(false);
        rightContainer.setAlignItems(Alignment.CENTER);

        Div questionIndicators = new Div();
        questionIndicators.getStyle()
                .setDisplay(Style.Display.GRID)
                .set("grid-template-columns", "repeat(2, 60px)")
                .set("gap", "20px")
                .set("justify-content", "center")
                .set("align-items", "center")
                .set("background-color", "#E3F7E9")
                .setBorderRadius("15px")
                .setPadding("20px")
                .setMarginBottom("30px");

        for (int i = 1; i <= getTotalQuestions(); i++) {
            Button indicator = new Button(String.valueOf(i));
            indicator.addClassName("lora-text");
            indicator.getStyle()
                    .setWidth("60px")
                    .setHeight("60px")
                    .set("background-color", i == questionNumber ? "#118B50" : "#C6ECC9")
                    .set("color", i == questionNumber ? "white" : "#333")
                    .setFontWeight("bold")
                    .setFontSize("18px")
                    .setBorder("none")
                    .setBorderRadius("15px")
                    .setCursor("pointer")
                    .setMargin("0")
                    .setBoxSizing(Style.BoxSizing.BORDER_BOX);

            int finalI = i;
            indicator.addClickListener(e -> {
                currentNoSoal = finalI;
                displayQuestion(finalI);
            });

            questionIndicators.add(indicator);
        }

        rightContainer.add(questionIndicators);

        Div spacer = new Div();
        spacer.getStyle().setFlexGrow("1");
        rightContainer.add(spacer);

        if (questionNumber >= getTotalQuestions()) {
            Button submitButton = new Button("Kirim");
            submitButton.addClassName("lora-text");
            submitButton.getStyle()
                    .setBackgroundColor("#118B50")
                    .setColor("white")
                    .setCursor("pointer")
                    .setBorderRadius("15px")
                    .setPadding("20px 40px")
                    .setBorder("none")
                    .setFontWeight("bold")
                    .setFontSize("18px")
                    .setMarginTop("20px")
                    .setBoxShadow("0 4px 8px rgba(17, 139, 80, 0.3)");

            submitButton.addClickListener(e -> {
                Notification.show("Latihan soal selesai", 2000, Notification.Position.TOP_CENTER);
            });

            rightContainer.add(submitButton);
        }

        contentLayout.add(leftContainer, rightContainer);
        mainContainer.add(contentLayout);
        mainContent.add(mainContainer);
    }

    private int getTotalQuestions() {
        return model.stream()
                .mapToInt(TryOutMediumModel::getNoSoal)
                .max()
                .orElse(0);
    }
}
