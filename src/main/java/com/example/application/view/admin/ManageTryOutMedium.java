package com.example.application.view.admin;

import com.example.application.components.Header;
import com.example.application.components.SidebarAdmin;
import com.example.application.dao.MateriDAO;
import com.example.application.dao.TryOutMediumDAO;
import com.example.application.model.TryOutMediumModel;
import com.example.application.model.UsersModel;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.vaadin.flow.component.notification.Notification.Position.MIDDLE;

@Route("try-out-medium")
@PageTitle("TryOut Medium - Admin")
public class ManageTryOutMedium extends VerticalLayout implements BeforeEnterObserver {

    private final TryOutMediumDAO tryOutMediumDAO = new TryOutMediumDAO();
    private final MateriDAO materiDAO = new MateriDAO();

    private Grid<TryOutMediumModel> soalGrid;
    private Integer selectedMapel;
    private List<TryOutMediumModel> currentSoalList = new ArrayList<>();

    public ManageTryOutMedium() {}

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        UsersModel user = VaadinSession.getCurrent().getAttribute(UsersModel.class);
        if (user == null) {
            event.forwardTo("login");
            Notification.show("Kamu Harus Login Terlebih dahulu!", 2000, MIDDLE);
        } else if (user.getRole().equalsIgnoreCase("user")) {
            event.forwardTo("dashboard");
            Notification.show("Kamu Bukan Admin!", 2000, MIDDLE);
        } else {
            buildLayout();
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

        VerticalLayout sidebar = SidebarAdmin.createSidebar();
        VerticalLayout mainContent = createMainContent();

        mainLayout.add(sidebar, mainContent);
        mainLayout.setFlexGrow(0, sidebar);
        mainLayout.setFlexGrow(1, mainContent);

        add(header, mainLayout);
        setFlexGrow(0, header);
        setFlexGrow(1, mainLayout);
    }

    private VerticalLayout createMainContent() {
        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setPadding(true);
        content.setSpacing(true);
        content.getStyle()
                .set("background-image", "url('images/background.jpg')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("background-repeat", "no-repeat");

        H2 title = new H2("TryOut Medium Management");
        title.addClassName("lora-text");
        title.getStyle().setColor("#118B50");

        HorizontalLayout levelLayout = createLevelNavigation();
        VerticalLayout mapelSection = createMapelSection();
        VerticalLayout soalSection = createSoalSection();

        content.add(title, levelLayout, mapelSection, soalSection);
        return content;
    }

    private HorizontalLayout createLevelNavigation() {
        HorizontalLayout levelLayout = new HorizontalLayout();
        levelLayout.setWidthFull();
        levelLayout.setSpacing(true);

        Button levelEasy = new Button("Easy", e -> UI.getCurrent().navigate("try-out-easy"));
        levelEasy.addClassNames("inter-text");
        levelEasy.getStyle()
                .setBackgroundColor("#6c757d")
                .setColor("white")
                .setFontWeight("bold")
                .setCursor("pointer");

        Button levelMedium = new Button("Medium");
        levelMedium.addClassNames("inter-text");
        levelMedium.getStyle()
                .setBackgroundColor("#118B50")
                .setColor("white")
                .setFontWeight("bold")
                .setCursor("pointer");

        Button levelHard = new Button("Hard", e -> UI.getCurrent().navigate("try-out-hard"));
        levelHard.addClassNames("inter-text");
        levelHard.getStyle()
                .setBackgroundColor("#6c757d")
                .setColor("white")
                .setFontWeight("bold")
                .setCursor("pointer");

        levelLayout.add(levelEasy, levelMedium, levelHard);
        return levelLayout;
    }

    private VerticalLayout createMapelSection() {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(true);

        H3 mapelTitle = new H3("Pilih Mata Pelajaran");
        mapelTitle.getStyle().setColor("#118B50");

        // Add statistics
        HorizontalLayout statsLayout = createStatsLayout();

        HorizontalLayout mapelLayout = new HorizontalLayout();
        mapelLayout.setSpacing(true);

        Button mtkButton = createMapelButton("Matematika", 1);
        Button bahasaInggrisButton = createMapelButton("Bahasa Inggris", 2);
        Button bahasaIndonesiaButton = createMapelButton("Bahasa Indonesia", 3);

        mapelLayout.add(mtkButton, bahasaInggrisButton, bahasaIndonesiaButton);

        section.add(mapelTitle, statsLayout, mapelLayout);
        return section;
    }

    private HorizontalLayout createStatsLayout() {
        HorizontalLayout statsLayout = new HorizontalLayout();
        statsLayout.setSpacing(true);
        statsLayout.getStyle().set("margin-bottom", "10px");

        Span mtkStats = createStatBadge("MTK: " + getQuestionCountForMapel(1) + " soal", "#118B50");
        Span engStats = createStatBadge("B.Inggris: " + getQuestionCountForMapel(2) + " soal", "#007bff");
        Span indStats = createStatBadge("B.Indonesia: " + getQuestionCountForMapel(3) + " soal", "#6f42c1");

        int total = getQuestionCountForMapel(1) + getQuestionCountForMapel(2) + getQuestionCountForMapel(3);
        Span totalStats = createStatBadge("Total: " + total + " soal", "#dc3545");

        statsLayout.add(mtkStats, engStats, indStats, totalStats);

        return statsLayout;
    }

    private Span createStatBadge(String text, String color) {
        Span badge = new Span(text);
        badge.getStyle()
                .set("background-color", color)
                .set("color", "white")
                .set("padding", "5px 10px")
                .set("border-radius", "15px")
                .set("font-size", "12px")
                .set("font-weight", "bold");
        return badge;
    }

    private Button createMapelButton(String namaMapel, int idMapel) {
        Button button = new Button(namaMapel);
        button.addThemeVariants(ButtonVariant.LUMO_LARGE);
        button.setWidth("200px");

        // Set default style (not selected)
        updateMapelButtonStyle(button, false);

        button.addClickListener(e -> {
            selectedMapel = idMapel;
            loadSoalData(idMapel);
            refreshMapelButtons();
        });

        return button;
    }

    private void updateMapelButtonStyle(Button button, boolean selected) {
        if (selected) {
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            button.getStyle().setBackgroundColor("#118B50");
        } else {
            button.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
            button.getStyle().setBackgroundColor("#6c757d");
        }
        button.getStyle().setColor("white");
    }

    private void updateAllMapelButtons() {
        // This method will be called to update button styles after selection
        // For now, we'll handle this through UI refresh
    }

    private void refreshMapelButtons() {
        // Simple notification instead of page reload
        if (selectedMapel != null) {
            String mapelName = switch (selectedMapel) {
                case 1 -> "Matematika";
                case 2 -> "Bahasa Inggris";
                case 3 -> "Bahasa Indonesia";
                default -> "Unknown";
            };
            Notification.show("Mata pelajaran " + mapelName + " dipilih", 2000, Notification.Position.BOTTOM_START);
        }
    }

    private int getQuestionCountForMapel(int idMapel) {
        try {
            // Use existing method from TryOutMediumDAO
            List<TryOutMediumModel> soalList = tryOutMediumDAO.getTryOutMediumByIdMapel(idMapel);
            return (int) soalList.stream()
                    .map(TryOutMediumModel::getIdSoalToMedium)
                    .distinct()
                    .count();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private VerticalLayout createSoalSection() {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(true);

        HorizontalLayout headerLayout = new HorizontalLayout();
        H3 soalTitle = new H3("Daftar Soal TryOut Medium");
        soalTitle.getStyle().setColor("#118B50");

        Button addButton = new Button("Tambah Soal", new Icon(VaadinIcon.PLUS));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> openSoalDialog(null));
        addButton.setEnabled(false);

        headerLayout.add(soalTitle, addButton);
        headerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        headerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        soalGrid = createSoalGrid();

        Span infoLabel = new Span("Pilih mata pelajaran terlebih dahulu untuk melihat dan mengelola soal");
        infoLabel.getStyle().set("font-style", "italic").set("color", "#666");

        section.add(headerLayout, infoLabel, soalGrid);
        return section;
    }

    private Grid<TryOutMediumModel> createSoalGrid() {
        Grid<TryOutMediumModel> grid = new Grid<>(TryOutMediumModel.class, false);
        grid.setHeight("400px");

        grid.addColumn(TryOutMediumModel::getNoSoal).setHeader("No").setWidth("80px");
        grid.addColumn(soal -> soal.getPertanyaan().length() > 50 ?
                        soal.getPertanyaan().substring(0, 50) + "..." : soal.getPertanyaan())
                .setHeader("Pertanyaan").setFlexGrow(1);
        grid.addColumn(TryOutMediumModel::getOpsi).setHeader("Kunci Jawaban").setWidth("80px");
        grid.addColumn(soal -> soal.getTextJawaban().length() > 30 ?
                        soal.getTextJawaban().substring(0, 30) + "..." : soal.getTextJawaban())
                .setHeader("Jawaban").setWidth("200px");

        grid.addComponentColumn(soal -> {
            HorizontalLayout actions = new HorizontalLayout();
            actions.setPadding(false);
            actions.setSpacing(true);

            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
            editButton.addClickListener(e -> openSoalDialog(soal));

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            deleteButton.addClickListener(e -> confirmDelete(soal));

            actions.add(editButton, deleteButton);
            return actions;
        }).setHeader("Aksi").setWidth("120px");

        return grid;
    }

    private void loadSoalData(int idMapel) {
        try {
            currentSoalList = tryOutMediumDAO.getTryOutMediumByIdMapel(idMapel);

            // Get unique questions with correct answers
            List<TryOutMediumModel> soalYangBenar = currentSoalList.stream()
                    .filter(s -> "ya".equalsIgnoreCase(s.getBenar()))
                    .collect(Collectors.toList());

            soalGrid.setItems(soalYangBenar);

            // Enable add button
            soalGrid.getParent().ifPresent(parent -> {
                if (parent instanceof VerticalLayout layout) {
                    layout.getChildren()
                            .filter(component -> component instanceof HorizontalLayout)
                            .findFirst()
                            .ifPresent(headerLayout -> {
                                ((HorizontalLayout) headerLayout).getChildren()
                                        .filter(component -> component instanceof Button)
                                        .forEach(button -> ((Button) button).setEnabled(true));
                            });
                }
            });

            String mapelName = switch (idMapel) {
                case 1 -> "Matematika";
                case 2 -> "Bahasa Inggris";
                case 3 -> "Bahasa Indonesia";
                default -> "Unknown";
            };

            Notification.show("Loaded " + soalYangBenar.size() + " soal untuk " + mapelName,
                    3000, Notification.Position.MIDDLE);
        } catch (SQLException e) {
            Notification.show("Error loading soal: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            e.printStackTrace();
        }
    }

    private void openSoalDialog(TryOutMediumModel soal) {
        if (selectedMapel == null) {
            Notification.show("Pilih mata pelajaran terlebih dahulu!", 3000, Notification.Position.MIDDLE);
            return;
        }

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(soal == null ? "Tambah Soal Baru" : "Edit Soal");
        dialog.setWidth("800px");
        dialog.setMaxWidth("90%");

        IntegerField noSoalField = createNomorSoalField();
        ComboBox<Integer> materiField = createMateriField();
        TextArea pertanyaanField = createPertanyaanField();
        List<TextField> optionFields = createOptionFields();
        RadioButtonGroup<String> correctAnswerGroup = createCorrectAnswerGroup();

        if (soal != null) {
            populateFieldsFromSoal(soal, noSoalField, materiField, pertanyaanField, optionFields, correctAnswerGroup);
        } else {
            setDefaultValuesForNewSoal(noSoalField, materiField, correctAnswerGroup);
        }

        VerticalLayout formLayout = new VerticalLayout(noSoalField, materiField, pertanyaanField);
        VerticalLayout answerLayout = new VerticalLayout(new H3("Pilihan Jawaban"));
        optionFields.forEach(answerLayout::add);
        answerLayout.add(correctAnswerGroup);

        dialog.add(new VerticalLayout(formLayout, answerLayout));

        Button saveButton = new Button("Simpan", e -> {
            if (validateSoalForm(soal, noSoalField, materiField, pertanyaanField, optionFields, correctAnswerGroup)) {
                saveSoal(soal, noSoalField.getValue(), materiField.getValue(),
                        pertanyaanField.getValue(), optionFields, correctAnswerGroup.getValue());
                dialog.close();
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Batal", e -> dialog.close());
        dialog.getFooter().add(cancelButton, saveButton);

        dialog.open();
    }

    private IntegerField createNomorSoalField() {
        IntegerField field = new IntegerField("Nomor Soal");
        field.setMin(1);
        field.setRequired(true);
        return field;
    }

    private ComboBox<Integer> createMateriField() {
        ComboBox<Integer> field = new ComboBox<>("ID Materi");
        field.setItems(1, 2, 3, 4, 5); // Sample materi IDs
        field.setRequired(true);
        field.setItemLabelGenerator(id -> "Materi " + id);
        return field;
    }

    private TextArea createPertanyaanField() {
        TextArea field = new TextArea("Pertanyaan");
        field.setRequired(true);
        field.setWidthFull();
        field.setHeight("120px");
        return field;
    }

    private List<TextField> createOptionFields() {
        List<TextField> fields = new ArrayList<>();
        for (char option = 'A'; option <= 'D'; option++) {
            TextField field = new TextField("Opsi " + option);
            field.setRequired(true);
            field.setWidthFull();
            fields.add(field);
        }
        return fields;
    }

    private RadioButtonGroup<String> createCorrectAnswerGroup() {
        RadioButtonGroup<String> group = new RadioButtonGroup<>();
        group.setLabel("Jawaban Benar");
        group.setItems("A", "B", "C", "D");
        group.setRequired(true);
        return group;
    }

    private void populateFieldsFromSoal(
            TryOutMediumModel soal,
            IntegerField noSoalField,
            ComboBox<Integer> materiField,
            TextArea pertanyaanField,
            List<TextField> optionFields,
            RadioButtonGroup<String> correctAnswerGroup
    ) {
        noSoalField.setValue(soal.getNoSoal());
        materiField.setValue(soal.getIdMateri());
        pertanyaanField.setValue(soal.getPertanyaan());

        // Get all answers for this question
        List<TryOutMediumModel> jawabanList = currentSoalList.stream()
                .filter(s -> s.getNoSoal() == soal.getNoSoal() && s.getIdMapel() == soal.getIdMapel())
                .sorted(Comparator.comparing(TryOutMediumModel::getOpsi))
                .collect(Collectors.toList());

        for (int i = 0; i < optionFields.size(); i++) {
            String option = String.valueOf((char) ('A' + i));
            TryOutMediumModel jawaban = jawabanList.stream()
                    .filter(j -> j.getOpsi().equals(option))
                    .findFirst()
                    .orElse(null);
            if (jawaban != null) {
                optionFields.get(i).setValue(jawaban.getTextJawaban());
                if ("ya".equalsIgnoreCase(jawaban.getBenar())) {
                    correctAnswerGroup.setValue(option);
                }
            } else {
                optionFields.get(i).setValue("");
            }
        }
    }

    private void setDefaultValuesForNewSoal(
            IntegerField noSoalField,
            ComboBox<Integer> materiField,
            RadioButtonGroup<String> correctAnswerGroup
    ) {
        // Simple approach to get next question number
        int nextNo = currentSoalList.stream()
                .mapToInt(TryOutMediumModel::getNoSoal)
                .max()
                .orElse(0) + 1;
        noSoalField.setValue(nextNo);

        materiField.setValue(1);
        correctAnswerGroup.setValue("A");
    }

    private boolean validateSoalForm(
            TryOutMediumModel existingSoal,
            IntegerField noSoal,
            ComboBox<Integer> materi,
            TextArea pertanyaan,
            List<TextField> options,
            RadioButtonGroup<String> correctAnswer
    ) {
        boolean valid = true;

        if (noSoal.isEmpty() || noSoal.getValue() <= 0) {
            Notification.show("Nomor soal harus diisi dan lebih dari 0!", 3000, Notification.Position.MIDDLE);
            valid = false;
        }

        if (materi.isEmpty()) {
            Notification.show("ID Materi harus dipilih!", 3000, Notification.Position.MIDDLE);
            valid = false;
        }

        if (pertanyaan.isEmpty() || pertanyaan.getValue().trim().length() < 10) {
            Notification.show("Pertanyaan harus diisi minimal 10 karakter!", 3000, Notification.Position.MIDDLE);
            valid = false;
        }

        for (int i = 0; i < options.size(); i++) {
            TextField option = options.get(i);
            if (option.isEmpty() || option.getValue().trim().length() < 2) {
                char optionLetter = (char) ('A' + i);
                Notification.show("Opsi " + optionLetter + " harus diisi minimal 2 karakter!", 3000, Notification.Position.MIDDLE);
                valid = false;
                break;
            }
        }

        if (correctAnswer.isEmpty()) {
            Notification.show("Pilih jawaban yang benar!", 3000, Notification.Position.MIDDLE);
            valid = false;
        }

        if (valid && noSoal.getValue() != null) {
            try {
                boolean isDuplicate = tryOutMediumDAO.isQuestionNumberUsed(
                        noSoal.getValue(),
                        selectedMapel,
                        existingSoal != null ? existingSoal.getIdSoalToMedium() : null
                );
                if (isDuplicate) {
                    Notification.show("Nomor soal " + noSoal.getValue() + " sudah digunakan!", 3000, Notification.Position.MIDDLE);
                    valid = false;
                }
            } catch (SQLException e) {
                Notification.show("Error validating question number: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
                valid = false;
            }
        }

        return valid;
    }

    private void saveSoal(
            TryOutMediumModel existingSoal,
            int noSoal,
            int idMateri,
            String pertanyaan,
            List<TextField> optionFields,
            String correctOption
    ) {
        try {
            if (existingSoal == null) {
                 tryOutMediumDAO.addSoalWithAnswers(noSoal, selectedMapel, idMateri, pertanyaan, optionFields, correctOption);
                 Notification.show("Soal berhasil ditambahkan!", 3000, Notification.Position.MIDDLE);
            } else {
                 tryOutMediumDAO.updateSoalWithAnswers(existingSoal.getIdSoalToMedium(), noSoal, pertanyaan, optionFields, correctOption);
                 Notification.show("Soal berhasil diperbarui!", 3000, Notification.Position.MIDDLE);
            }

            // loadSoalData(selectedMapel);
        } catch (Exception e) {
            Notification.show("Error menyimpan soal: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            e.printStackTrace();
        }
    }

    private void confirmDelete(TryOutMediumModel soal) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Konfirmasi Hapus");
        dialog.setText("Apakah Anda yakin ingin menghapus soal nomor " + soal.getNoSoal() + "?");

        dialog.setCancelable(true);
        dialog.setConfirmText("Hapus");
        dialog.setConfirmButtonTheme("error primary");

        dialog.addConfirmListener(e -> {
            try {
                 tryOutMediumDAO.deleteSoalWithAnswers(soal.getIdSoalToMedium());
                 Notification.show("Soal berhasil dihapus!", 3000, MIDDLE);
                 loadSoalData(selectedMapel);
            } catch (Exception ex) {
                Notification.show("Error menghapus soal: " + ex.getMessage(), 5000, MIDDLE);
                ex.printStackTrace();
            }
        });

        dialog.open();
    }
}