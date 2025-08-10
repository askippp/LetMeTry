package com.example.application.view.admin;

import com.example.application.components.Header;
import com.example.application.components.SidebarAdmin;
import com.example.application.dao.MateriDAO;
import com.example.application.dao.TryOutEasyDAO;
import com.example.application.model.MateriModel;
import com.example.application.model.TryOutEasyModel;
import com.example.application.model.UsersModel;
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
import java.util.List;

import static com.vaadin.flow.component.notification.Notification.Position.MIDDLE;

@Route("try-out-easy")
@PageTitle("TryOut Easy - Admin")
public class ManageTryOutEasy extends VerticalLayout implements BeforeEnterObserver {

    private final TryOutEasyDAO tryOutEasyDAO = new TryOutEasyDAO();
    private final MateriDAO materiDAO = new MateriDAO();

    private Grid<TryOutEasyModel> soalGrid;
    private Integer selectedMapel;
    private List<TryOutEasyModel> currentSoalList = new ArrayList<>();

    public ManageTryOutEasy() {}

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

        H2 title = new H2("TryOut Easy Management");
        title.addClassName("lora-text");
        title.getStyle().setColor("#118B50");

        VerticalLayout mapelSection = createMapelSection();
        VerticalLayout soalSection = createSoalSection();

        content.add(title, mapelSection, soalSection);
        return content;
    }

    private VerticalLayout createMapelSection() {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(true);

        H3 mapelTitle = new H3("Pilih Mata Pelajaran");
        mapelTitle.addClassName("lora-text");
        mapelTitle.getStyle().setColor("#118B50");

        HorizontalLayout mapelLayout = new HorizontalLayout();
        mapelLayout.setSpacing(true);

        HorizontalLayout matematikaCard = createMapelCard("Matematika", 1);
        HorizontalLayout bahasaInggrisCard = createMapelCard("Bahasa Inggris", 2);
        HorizontalLayout bahasaIndonesiaCard = createMapelCard("Bahasa Indonesia", 3);

        mapelLayout.add(matematikaCard, bahasaInggrisCard, bahasaIndonesiaCard);

        section.add(mapelTitle, mapelLayout);
        return section;
    }

    private HorizontalLayout createMapelCard(String namaMapel, int idMapel) {
        HorizontalLayout card = new HorizontalLayout();
        card.setPadding(true);
        card.setSpacing(true);
        card.getStyle()
                .setBackgroundColor("#FFFFFF")
                .setBorderRadius("10px")
                .setBorder("2px solid #d1d5db")
                .setPadding("15px")
                .setCursor("pointer")
                .setMinWidth("200px");

        VerticalLayout cardContent = new VerticalLayout();
        cardContent.setPadding(false);
        cardContent.setSpacing(false);

        Span mapelName = new Span(namaMapel);
        mapelName.getStyle()
                .setFontSize("1.1rem")
                .setFontWeight("bold")
                .setColor("#118B50");

        Span questionCount = new Span();
        questionCount.getStyle()
                .setFontSize("0.9rem")
                .setColor("#666");

        // Get question count
        try {
            int count = tryOutEasyDAO.getQuestionCountByMapel(idMapel);
            questionCount.setText(count + " soal tersedia");
            if (count == 0) {
                questionCount.getStyle().setColor("#dc3545");
            } else {
                questionCount.getStyle().setColor("#28a745");
            }
        } catch (SQLException e) {
            questionCount.setText("Error loading count");
            questionCount.getStyle().setColor("#dc3545");
        }

        cardContent.add(mapelName, questionCount);
        card.add(cardContent);

        card.addClickListener(e -> {
            selectedMapel = idMapel;
            loadSoalData(idMapel);
            updateSelectedMapelUI(namaMapel);
        });

        return card;
    }

    private void updateSelectedMapelUI(String namaMapel) {
        soalGrid.getParent().ifPresent(parent -> {
            if (parent instanceof VerticalLayout layout) {
                layout.getChildren()
                        .filter(component -> component instanceof HorizontalLayout)
                        .findFirst()
                        .ifPresent(headerLayout -> {
                            headerLayout.getChildren()
                                    .filter(component -> component instanceof Button)
                                    .forEach(button -> ((Button) button).setEnabled(true));
                        });
            }
        });
    }

    private VerticalLayout createSoalSection() {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(true);

        HorizontalLayout headerLayout = new HorizontalLayout();
        H3 soalTitle = new H3("Daftar Soal TryOut Easy");
        soalTitle.addClassName("lora-text");
        soalTitle.getStyle().setColor("#118B50");

        Button addButton = new Button("Buat Soal Baru", new Icon(VaadinIcon.PLUS));
        addButton.addClassNames("inter-text");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> openCreateSoalDialog());
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

    private Grid<TryOutEasyModel> createSoalGrid() {
        Grid<TryOutEasyModel> grid = new Grid<>(TryOutEasyModel.class, false);
        grid.setHeight("300px");

        grid.addColumn(TryOutEasyModel::getNoSoal).setHeader("No").setWidth("80px");
        grid.addColumn(soal -> soal.getPertanyaan().length() > 50 ?
                        soal.getPertanyaan().substring(0, 50) + "..." : soal.getPertanyaan())
                .setHeader("Pertanyaan").setFlexGrow(1);
        grid.addColumn(TryOutEasyModel::getOpsi).setHeader("Kunci Jawaban").setWidth("80px");
        grid.addColumn(soal -> soal.getTextJawaban().length() > 30 ?
                        soal.getTextJawaban().substring(0, 30) + "..." : soal.getTextJawaban())
                .setHeader("Jawaban").setWidth("200px");

        grid.addComponentColumn(soal -> {
            HorizontalLayout actions = new HorizontalLayout();
            actions.setPadding(false);
            actions.setSpacing(true);

            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
            editButton.addClickListener(event -> {
                try {
                    openEditSoalDialog(soal);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

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
            currentSoalList = tryOutEasyDAO.getSoalByMapel(idMapel);
            soalGrid.setItems(currentSoalList);
        } catch (SQLException e) {
            Notification.show("Error loading soal: " + e.getMessage(), 3000, MIDDLE);
            e.printStackTrace();
        }
    }

    private void openCreateSoalDialog() {
        if (selectedMapel == null) {
            Notification.show("Pilih mata pelajaran terlebih dahulu!", 3000, MIDDLE);
            return;
        }

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Buat Soal TryOut Easy Baru");
        dialog.setWidth("600px");
        dialog.setMaxWidth("90%");

        VerticalLayout dialogContent = new VerticalLayout();

        // Number of questions field
        IntegerField totalSoalField = new IntegerField("Jumlah Soal");
        totalSoalField.setMin(1);
        totalSoalField.setMax(50);
        totalSoalField.setValue(1);
        totalSoalField.setRequired(true);

        dialogContent.add(totalSoalField);
        dialog.add(dialogContent);

        Button nextButton = new Button("Lanjut", e -> {
            if (totalSoalField.getValue() != null) {
                dialog.close();
                openMultiSoalDialog(totalSoalField.getValue());
            } else {
                Notification.show("Masukkan jumlah soal!", 3000, MIDDLE);
            }
        });
        nextButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Batal", e -> dialog.close());
        dialog.getFooter().add(cancelButton, nextButton);

        dialog.open();
    }

    private void openMultiSoalDialog(int totalSoal) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Input " + totalSoal + " Soal");
        dialog.setWidth("900px");
        dialog.setMaxWidth("95%");
        dialog.setHeight("80%");

        VerticalLayout scrollableContent = new VerticalLayout();
        scrollableContent.setPadding(false);
        scrollableContent.setSpacing(true);
        scrollableContent.getStyle().set("overflow-y", "auto");

        List<List<TextField>> allOptionFields = new ArrayList<>();
        List<TextArea> pertanyaanFields = new ArrayList<>();
        List<RadioButtonGroup<String>> correctAnswerGroups = new ArrayList<>();
        List<ComboBox<MateriModel>> materiCombos = new ArrayList<>();

        // Load all materi for selected mapel
        List<MateriModel> allMateri = materiDAO.getAllMateriByMapel(selectedMapel);

        for (int i = 0; i < totalSoal; i++) {
            VerticalLayout soalContainer = new VerticalLayout();
            soalContainer.setPadding(true);
            soalContainer.getStyle()
                    .setBorder("1px solid #ddd")
                    .setBorderRadius("5px")
                    .setMarginBottom("10px");

            H3 soalTitle = new H3("Soal #" + (i + 1));
            soalTitle.getStyle().setMargin("0");

            // Materi selection for this question
            ComboBox<MateriModel> materiCombo = new ComboBox<>("Materi");
            materiCombo.setItemLabelGenerator(materiModel ->
                    materiModel.getKelas() + " - " + materiModel.getNamaMateri()
            );
            materiCombo.setItems(allMateri);
            materiCombo.setRequired(true);
            materiCombos.add(materiCombo);

            TextArea pertanyaanField = new TextArea("Pertanyaan");
            pertanyaanField.setWidthFull();
            pertanyaanField.setRequired(true);
            pertanyaanFields.add(pertanyaanField);

            List<TextField> optionFields = new ArrayList<>();
            for (int j = 1; j <= 5; j++) { // Changed to 5 options (A-E)
                TextField optionField = new TextField("Opsi " + (char)('A' + j - 1));
                optionField.setWidthFull();
                optionField.setRequired(true);
                optionFields.add(optionField);
            }
            allOptionFields.add(optionFields);

            RadioButtonGroup<String> correctAnswerGroup = new RadioButtonGroup<>();
            correctAnswerGroup.setLabel("Pilih Jawaban Benar");
            correctAnswerGroup.setItems("A", "B", "C", "D", "E"); // Changed to include E
            correctAnswerGroup.setRequired(true);
            correctAnswerGroups.add(correctAnswerGroup);

            VerticalLayout optionsLayout = new VerticalLayout();
            optionsLayout.setPadding(false);
            optionsLayout.setSpacing(true);
            optionFields.forEach(optionsLayout::add);

            soalContainer.add(soalTitle, materiCombo, pertanyaanField, optionsLayout, correctAnswerGroup);
            scrollableContent.add(soalContainer);
        }

        dialog.add(scrollableContent);

        Button saveAllButton = new Button("Simpan", e -> {
            if (validateAllSoal(pertanyaanFields, allOptionFields, correctAnswerGroups, materiCombos)) {
                saveAllSoal(pertanyaanFields, allOptionFields, correctAnswerGroups, materiCombos);
                dialog.close();
            }
        });
        saveAllButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Batal", e -> dialog.close());
        dialog.getFooter().add(cancelButton, saveAllButton);

        dialog.open();
    }

    private boolean validateAllSoal(
            List<TextArea> pertanyaanFields,
            List<List<TextField>> allOptionFields,
            List<RadioButtonGroup<String>> correctAnswerGroups,
            List<ComboBox<MateriModel>> materiCombos
    ) {
        for (int i = 0; i < pertanyaanFields.size(); i++) {
            if (pertanyaanFields.get(i).getValue().trim().isEmpty()) {
                Notification.show("Pertanyaan No. " + (i+1) + " tidak boleh kosong!", 3000, MIDDLE);
                return false;
            }

            if (materiCombos.get(i).getValue() == null) {
                Notification.show("Pilih materi untuk soal No. " + (i+1), 3000, MIDDLE);
                return false;
            }

            List<TextField> options = allOptionFields.get(i);
            for (int j = 0; j < options.size(); j++) {
                if (options.get(j).getValue().trim().isEmpty()) {
                    Notification.show("Opsi " + (char)('A'+j) + " untuk soal No. " + (i+1) + " tidak boleh kosong!", 3000, MIDDLE);
                    return false;
                }
            }

            if (correctAnswerGroups.get(i).getValue() == null) {
                Notification.show("Pilih kunci jawaban untuk soal No. " + (i+1), 3000, MIDDLE);
                return false;
            }
        }
        return true;
    }

    private void saveAllSoal(
            List<TextArea> pertanyaanFields,
            List<List<TextField>> allOptionFields,
            List<RadioButtonGroup<String>> correctAnswerGroups,
            List<ComboBox<MateriModel>> materiCombos
    ) {
        try {
            List<TryOutEasyModel> soalList = new ArrayList<>();

            for (int i = 0; i < pertanyaanFields.size(); i++) {
                TryOutEasyModel soal = new TryOutEasyModel();
                soal.setIdMapel(selectedMapel);
                soal.setIdMateri(materiCombos.get(i).getValue().getIdMateri());
                soal.setNoSoal(i + 1);
                soal.setPertanyaan(pertanyaanFields.get(i).getValue());

                String kunciJawaban = correctAnswerGroups.get(i).getValue();
                soal.setOpsi(kunciJawaban);

                // Set text jawaban sesuai dengan jawaban yang benar
                List<TextField> options = allOptionFields.get(i);
                int correctIndex = kunciJawaban.charAt(0) - 'A';
                soal.setTextJawaban(options.get(correctIndex).getValue());
                soal.setBenar("ya");

                soalList.add(soal);
            }

            tryOutEasyDAO.insertMultipleSoal(soalList);
            Notification.show("Berhasil menyimpan " + soalList.size() + " soal!", 3000, MIDDLE);
            loadSoalData(selectedMapel);
        } catch (SQLException e) {
            Notification.show("Gagal menyimpan soal: " + e.getMessage(), 3000, MIDDLE);
            e.printStackTrace();
        }
    }

    private void openEditSoalDialog(TryOutEasyModel soal) throws SQLException {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Edit Soal TryOut Easy - No. " + soal.getNoSoal());
        dialog.setWidth("650px");
        dialog.setMaxWidth("90%");

        VerticalLayout dialogContent = new VerticalLayout();
        dialogContent.setSpacing(true);

        // ComboBox Materi
        ComboBox<MateriModel> materiCombo = new ComboBox<>("Materi");
        materiCombo.setItemLabelGenerator(materiModel ->
                materiModel.getNamaMateri() + " - " + materiModel.getKelas()
        );
        materiCombo.setRequired(true);

        // Load current materi
        MateriModel currentMateri = materiDAO.getMateriById(soal.getIdMateri());
        if (currentMateri != null) {
            List<MateriModel> allMateri = materiDAO.getAllMateriByMapel(selectedMapel);
            materiCombo.setItems(allMateri);
            materiCombo.setValue(currentMateri);
        }

        // Question field
        TextArea pertanyaanField = new TextArea("Pertanyaan");
        pertanyaanField.setWidthFull();
        pertanyaanField.setMaxHeight("120px");
        pertanyaanField.setValue(soal.getPertanyaan() != null ? soal.getPertanyaan() : "");
        pertanyaanField.setRequired(true);

        // Load all answers
        List<TextField> optionFields = new ArrayList<>();
        List<String> jawabanList = tryOutEasyDAO.getAllJawabanForEdit(soal.getIdSoalToEasy());

        // Create option fields A-E
        for (int i = 0; i < 5; i++) {
            char opsiLabel = (char)('A' + i);
            TextField optionField = new TextField("Opsi " + opsiLabel);
            optionField.setWidthFull();
            optionField.setRequired(true);

            // Set value from database
            if (i < jawabanList.size()) {
                optionField.setValue(jawabanList.get(i) != null ? jawabanList.get(i) : "");
            }

            optionFields.add(optionField);
        }

        // Correct answer radio button
        RadioButtonGroup<String> correctAnswerGroup = new RadioButtonGroup<>();
        correctAnswerGroup.setLabel("Jawaban Benar");
        correctAnswerGroup.setItems("A", "B", "C", "D", "E");
        correctAnswerGroup.setValue(soal.getOpsi() != null ? soal.getOpsi() : "A");
        correctAnswerGroup.setRequired(true);

        // Layout for answer options
        VerticalLayout optionsLayout = new VerticalLayout();
        optionsLayout.setPadding(false);
        optionsLayout.setSpacing(true);
        optionFields.forEach(optionsLayout::add);

        // Add components to dialog
        dialogContent.add(materiCombo, pertanyaanField, optionsLayout, correctAnswerGroup);
        dialog.add(dialogContent);

        // Save button
        Button saveButton = new Button("Simpan", e -> {
            // Validation
            if (pertanyaanField.isEmpty() ||
                    optionFields.stream().anyMatch(field -> field.getValue().trim().isEmpty()) ||
                    correctAnswerGroup.isEmpty() ||
                    materiCombo.isEmpty()) {
                Notification.show("Lengkapi semua field!", 3000, MIDDLE);
                return;
            }

            try {
                // Update soal object
                soal.setPertanyaan(pertanyaanField.getValue().trim());
                soal.setIdMateri(materiCombo.getValue().getIdMateri());
                soal.setOpsi(correctAnswerGroup.getValue());

                // Set text jawaban yang benar
                int correctIndex = correctAnswerGroup.getValue().charAt(0) - 'A';
                soal.setTextJawaban(optionFields.get(correctIndex).getValue().trim());

                // Collect all answers from form
                List<String> jawabanUpdate = optionFields.stream()
                        .map(field -> field.getValue().trim())
                        .collect(java.util.stream.Collectors.toList());

                // Update database
                tryOutEasyDAO.updateSoalAndJawaban(soal, jawabanUpdate);

                Notification.show("Soal berhasil diupdate!", 3000, MIDDLE);
                loadSoalData(selectedMapel); // Refresh grid
                dialog.close();

            } catch (SQLException ex) {
                Notification.show("Gagal update soal: " + ex.getMessage(), 5000, MIDDLE);
                ex.printStackTrace();
            } catch (Exception ex) {
                Notification.show("Terjadi kesalahan: " + ex.getMessage(), 5000, MIDDLE);
                ex.printStackTrace();
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Batal", e -> dialog.close());
        dialog.getFooter().add(cancelButton, saveButton);

        dialog.open();
    }

    private void confirmDelete(TryOutEasyModel soal) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Hapus Soal");
        dialog.setText("Apakah Anda yakin ingin menghapus soal ini?");

        dialog.setCancelable(true);
        dialog.setConfirmText("Hapus");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(e -> {
            try {
                tryOutEasyDAO.deleteSoal(soal.getIdSoalToEasy());
                Notification.show("Soal berhasil dihapus!", 3000, MIDDLE);
                loadSoalData(selectedMapel);
            } catch (SQLException ex) {
                Notification.show("Gagal menghapus soal: " + ex.getMessage(), 3000, MIDDLE);
                ex.printStackTrace();
            }
        });

        dialog.open();
    }
}