package com.example.application.view.admin;

import com.example.application.components.Header;
import com.example.application.components.SidebarAdmin;
import com.example.application.dao.LatihanSoalDAO;
import com.example.application.dao.MateriDAO;
import com.example.application.model.LatihanSoalModel;
import com.example.application.model.MateriModel;
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
import java.util.*;

import static com.vaadin.flow.component.notification.Notification.Position.MIDDLE;

@Route("latihan-soal")
@PageTitle("Latihan - Soal - Admin")
public class LatihanSoal extends VerticalLayout implements BeforeEnterObserver {

    private final LatihanSoalDAO latihanSoalDAO = new LatihanSoalDAO();
    private final MateriDAO materiDAO = new MateriDAO();

    private Grid<MateriModel> materiGrid;
    private Grid<LatihanSoalModel> soalGrid;
    private MateriModel selectedMateri;
    private List<LatihanSoalModel> currentSoalList = new ArrayList<>();

    public LatihanSoal() {}

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

        H2 title = new H2("Latihan Soal Management");
        title.addClassName("lora-text");
        title.getStyle().setColor("#118B50");

        VerticalLayout materiSection = createMateriSection();

        VerticalLayout soalSection = createSoalSection();

        content.add(title, materiSection, soalSection);
        return content;
    }

    private void loadMateriData(Integer idMapel, String kelas) {
        if (idMapel != null && kelas != null) {
            List<MateriModel> materiList = materiDAO.getAllMateriMtkSepuluh(idMapel, kelas);
            materiGrid.setItems(materiList);
        }
    }

    private int getQuestionCountForMateri(int idMateri) {
        try {
            List<LatihanSoalModel> soalList = latihanSoalDAO.getSoalByIdMateri(idMateri);
            return (int) soalList.stream()
                    .map(LatihanSoalModel::getIdSoalLatsol)
                    .distinct()
                    .count();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private VerticalLayout createMateriSection() {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(true);

        H3 materiTitle = new H3("Daftar Materi");
        materiTitle.getStyle().setColor("#118B50");

        HorizontalLayout filterLayout = new HorizontalLayout();
        ComboBox<String> kelasFilter = new ComboBox<>("Kelas");
        kelasFilter.setItems("10", "11", "12");
        kelasFilter.setValue("10");
        kelasFilter.setWidth("150px");

        ComboBox<Integer> mapelFilter = new ComboBox<>("Mata Pelajaran");
        mapelFilter.setItems(1, 2, 3);
        mapelFilter.setValue(1);
        mapelFilter.setWidth("200px");

        mapelFilter.setItemLabelGenerator(id -> {
            return switch (id) {
                case 1 -> "Matematika";
                case 2 -> "Bahasa Inggris";
                case 3 -> "Bahasa Indonesia";
                default -> "Unknown";
            };
        });

        Button refreshButton = new Button("Refresh", new Icon(VaadinIcon.REFRESH));
        refreshButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        refreshButton.addClickListener(e -> loadMateriData(mapelFilter.getValue(), kelasFilter.getValue()));

        filterLayout.add(kelasFilter, mapelFilter, refreshButton);
        filterLayout.setDefaultVerticalComponentAlignment(Alignment.END);

        materiGrid = createMateriGrid();

        loadMateriData(1, "10");

        section.add(materiTitle, filterLayout, materiGrid);
        return section;
    }

    private Grid<MateriModel> createMateriGrid() {
        Grid<MateriModel> grid = new Grid<>(MateriModel.class, false);
        grid.setHeight("300px");

        grid.addColumn(MateriModel::getIdMateri).setHeader("ID").setWidth("80px");
        grid.addColumn(MateriModel::getNamaMateri).setHeader("Nama Materi").setFlexGrow(1);
        grid.addColumn(MateriModel::getKelas).setHeader("Kelas").setWidth("100px");

        grid.addComponentColumn(materi -> {
            Span status = new Span();
            int questionCount = getQuestionCountForMateri(materi.getIdMateri());
            if (questionCount > 0) {
                status.setText(questionCount + " soal");
                status.getElement().getThemeList().add("badge success");
                status.getStyle().set("background-color", "#118B50").set("color", "white");
            } else {
                status.setText("Belum ada soal");
                status.getElement().getThemeList().add("badge error");
                status.getStyle().set("background-color", "#dc3545").set("color", "white");
            }
            return status;
        }).setHeader("Status").setWidth("150px");

        grid.addComponentColumn(materi -> {
            Button manageButton = new Button("Kelola Soal", new Icon(VaadinIcon.EDIT));
            manageButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);
            manageButton.addClickListener(e -> {
                selectedMateri = materi;
                loadSoalData(materi.getIdMateri());
            });
            return manageButton;
        }).setHeader("Aksi").setWidth("120px");

        return grid;
    }

    private VerticalLayout createSoalSection() {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(true);

        HorizontalLayout headerLayout = new HorizontalLayout();
        H3 soalTitle = new H3("Daftar Soal");
        soalTitle.getStyle().setColor("#118B50");

        Button addButton = new Button("Tambah Soal", new Icon(VaadinIcon.PLUS));
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> openSoalDialog(null));
        addButton.setEnabled(false);
        headerLayout.add(soalTitle, addButton);
        headerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        headerLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        soalGrid = createSoalGrid();

        Span infoLabel = new Span("Pilih materi terlebih dahulu untuk melihat dan mengelola soal");
        infoLabel.getStyle().set("font-style", "italic").set("color", "#666");

        section.add(headerLayout, infoLabel, soalGrid);
        return section;
    }

    private Grid<LatihanSoalModel> createSoalGrid() {
        Grid<LatihanSoalModel> grid = new Grid<>(LatihanSoalModel.class, false);
        grid.setHeight("400px");

        grid.addColumn(LatihanSoalModel::getNoSoal).setHeader("No").setWidth("80px");
        grid.addColumn(soal -> soal.getPertanyaan().length() > 50 ?
                        soal.getPertanyaan().substring(0, 50) + "..." : soal.getPertanyaan())
                .setHeader("Pertanyaan").setFlexGrow(1);
        grid.addColumn(LatihanSoalModel::getOpsi).setHeader("Kunci Jawaban").setWidth("80px");
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

    private void loadSoalData(int idMateri) {
        try {
            currentSoalList = latihanSoalDAO.getSoalByIdMateri(idMateri);

            List<LatihanSoalModel> soalYangBenar = currentSoalList.stream()
                    .filter(s -> "ya".equalsIgnoreCase(s.getBenar()))
                    .toList();

            soalGrid.setItems(soalYangBenar);

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

            Notification.show("Loaded " + soalYangBenar.size() + " soal untuk materi: " + selectedMateri.getNamaMateri(),
                    3000, Notification.Position.MIDDLE);
        } catch (SQLException e) {
            Notification.show("Error loading soal: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            e.printStackTrace();
        }
    }

    private void openSoalDialog(LatihanSoalModel soal) {
        if (selectedMateri == null) {
            Notification.show("Pilih materi terlebih dahulu!", 3000, Notification.Position.MIDDLE);
            return;
        }

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(soal == null ? "Tambah Soal Baru" : "Edit Soal");
        dialog.setWidth("800px");
        dialog.setMaxWidth("90%");

        IntegerField noSoalField = createNomorSoalField();
        TextArea pertanyaanField = createPertanyaanField();
        List<TextField> optionFields = createOptionFields();
        RadioButtonGroup<String> correctAnswerGroup = createCorrectAnswerGroup();

        if (soal != null) {
            populateFieldsFromSoal(soal, noSoalField, pertanyaanField, optionFields, correctAnswerGroup);
        } else {
            setDefaultValuesForNewSoal(noSoalField, correctAnswerGroup);
        }

        VerticalLayout formLayout = new VerticalLayout(noSoalField, pertanyaanField);
        VerticalLayout answerLayout = new VerticalLayout(new H3("Pilihan Jawaban"));
        optionFields.forEach(answerLayout::add);
        answerLayout.add(correctAnswerGroup);

        dialog.add(new VerticalLayout(formLayout, answerLayout));

        Button saveButton = new Button("Simpan", e -> {
            if (validateSoalForm(soal, noSoalField, pertanyaanField, optionFields, correctAnswerGroup)) {
                saveSoal(soal, noSoalField.getValue(), pertanyaanField.getValue(), optionFields, correctAnswerGroup.getValue());
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
            LatihanSoalModel soal,
            IntegerField noSoalField,
            TextArea pertanyaanField,
            List<TextField> optionFields,
            RadioButtonGroup<String> correctAnswerGroup
    ) {
        noSoalField.setValue(soal.getNoSoal());
        pertanyaanField.setValue(soal.getPertanyaan());

        List<LatihanSoalModel> jawabanList = currentSoalList.stream()
                .filter(s -> s.getNoSoal() == soal.getNoSoal() && s.getIdMateri() == soal.getIdMateri())
                .sorted(Comparator.comparing(LatihanSoalModel::getOpsi))
                .toList();

        for (int i = 0; i < optionFields.size(); i++) {
            String option = String.valueOf((char) ('A' + i));
            LatihanSoalModel jawaban = jawabanList.stream()
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

    private void setDefaultValuesForNewSoal(IntegerField noSoalField, RadioButtonGroup<String> correctAnswerGroup) {
        int nextNo = currentSoalList.stream()
                .mapToInt(LatihanSoalModel::getNoSoal)
                .max()
                .orElse(0) + 1;
        noSoalField.setValue(nextNo);
        correctAnswerGroup.setValue("A");
    }

    private boolean validateSoalForm(
            LatihanSoalModel existingSoal,
            IntegerField noSoal,
            TextArea pertanyaan,
            List<TextField> options,
            RadioButtonGroup<String> correctAnswer
    ) {
        boolean valid = true;

        if (noSoal.isEmpty() || noSoal.getValue() <= 0) {
            Notification.show("Nomor soal harus diisi dan lebih dari 0!", 3000, Notification.Position.MIDDLE);
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
            boolean isDuplicate = currentSoalList.stream()
                    .anyMatch(soal -> soal.getNoSoal() == noSoal.getValue()
                            && (existingSoal == null || soal.getIdSoalLatsol() != existingSoal.getIdSoalLatsol()));
            if (isDuplicate) {
                Notification.show("Nomor soal " + noSoal.getValue() + " sudah digunakan!", 3000, Notification.Position.MIDDLE);
                valid = false;
            }
        }

        return valid;
    }

    private void saveSoal(
            LatihanSoalModel existingSoal, int noSoal, String pertanyaan,
            List<TextField> optionFields, String correctOption
    ) {
        try {
            if (existingSoal == null) {
                latihanSoalDAO.addSoalWithAnswers(noSoal, selectedMateri.getIdMapel(),
                        selectedMateri.getIdMateri(), pertanyaan, optionFields, correctOption);
                Notification.show("Soal berhasil ditambahkan!", 3000, Notification.Position.MIDDLE);
            } else {
                latihanSoalDAO.updateSoalWithAnswers(existingSoal.getIdSoalLatsol(), noSoal,
                        pertanyaan, optionFields, correctOption);
                Notification.show("Soal berhasil diperbarui!", 3000, Notification.Position.MIDDLE);
            }

            loadSoalData(selectedMateri.getIdMateri());
            materiGrid.getDataProvider().refreshAll();
        } catch (Exception e) {
            Notification.show("Error menyimpan soal: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            e.printStackTrace();
        }
    }

    private void confirmDelete(LatihanSoalModel soal) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Konfirmasi Hapus");
        dialog.setText("Apakah Anda yakin ingin menghapus soal nomor " + soal.getNoSoal() + "?");

        dialog.setCancelable(true);
        dialog.setConfirmText("Hapus");
        dialog.setConfirmButtonTheme("error primary");

        dialog.addConfirmListener(e -> {
            try {
                latihanSoalDAO.deleteSoalWithAnswers(soal.getIdSoalLatsol());
                Notification.show("Soal berhasil dihapus!", 3000, MIDDLE);
                loadSoalData(selectedMateri.getIdMateri());
                materiGrid.getDataProvider().refreshAll();
            } catch (Exception ex) {
                Notification.show("Error menghapus soal: " + ex.getMessage(), 5000, MIDDLE);
                ex.printStackTrace();
            }
        });

        dialog.open();
    }
}