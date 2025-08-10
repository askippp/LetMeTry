package com.example.application.view.user;

import com.example.application.components.Header;
import com.example.application.dao.UsersDAO;
import com.example.application.model.UsersModel;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER;

@Route("profile")
@PageTitle("Profile")
public class UserProfile extends VerticalLayout implements BeforeEnterObserver {

    private Image avatar;
    private Upload upload;
    private UsersModel currentUser;

    public UserProfile() {}

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

        getStyle()
                .set("background-image", "url('images/background.jpg')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("background-repeat", "no-repeat");

        currentUser = VaadinSession.getCurrent().getAttribute(UsersModel.class);

        HorizontalLayout header = Header.headerWithBackButton("Back");
        VerticalLayout mainContent = createProfileLayout();

        add(header, mainContent);
    }

    private VerticalLayout createProfileLayout() {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSpacing(false);
        mainLayout.setPadding(true);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        Div profileCard = new Div();
        profileCard.getStyle()
                .setBackgroundColor("#C5F3D0")
                .setBorderRadius("20px")
                .setPadding("30px")
                .setMarginTop("10%")
                .setWidth("600px")
                .setPosition(Style.Position.RELATIVE);

        Div avatarContainer = new Div();
        avatarContainer.getStyle()
                .setPosition(Style.Position.ABSOLUTE)
                .setTop("-100px")
                .setLeft("50%")
                .setTransform("TranslateX(-50%)")
                .setWidth("200px")
                .setHeight("200px")
                .setBackgroundColor("#FFF")
                .setBorderRadius("50%")
                .setDisplay(Style.Display.FLEX)
                .setJustifyContent(Style.JustifyContent.CENTER)
                .setAlignItems(Style.AlignItems.CENTER)
                .setBoxShadow("0 2px 8px rgba(0,0,0,0.1)");

        String username = currentUser != null ? currentUser.getUsername() : "Guest";
        avatar = createAvatarImage();

        Div plusButton = new Div();
        plusButton.getStyle()
                .setPosition(Style.Position.ABSOLUTE)
                .setBottom("10px")
                .setRight("20px")
                .setWidth("35px")
                .setHeight("35px")
                .setBackgroundColor("#333")
                .setBorderRadius("50%")
                .setDisplay(Style.Display.FLEX)
                .setAlignItems(Style.AlignItems.CENTER)
                .setJustifyContent(Style.JustifyContent.CENTER)
                .setCursor("pointer");

        Icon plusIcon = new Icon(VaadinIcon.PLUS);
        plusIcon.setSize("25x");
        plusIcon.setColor("white");
        plusButton.add(plusIcon);

        setupUpload();

        plusButton.addClickListener(e -> {
            upload.getElement().executeJs("this.shadowRoot.querySelector('input[type=file]').click()");
        });

        avatarContainer.add(avatar, plusButton, upload);

        Div profileInfo = new Div();
        profileInfo.getStyle()
                .setMarginTop("80px")
                .setWidth("100%");

        HorizontalLayout infoLayout = new HorizontalLayout();
        infoLayout.setWidthFull();
        infoLayout.setPadding(true);
        infoLayout.setSpacing(true);
        infoLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        VerticalLayout profileContainer = new VerticalLayout();
        profileContainer.addClassName("lora-text");
        profileContainer.setSpacing(false);
        profileContainer.setPadding(false);
        profileContainer.setAlignItems(Alignment.START);

        HorizontalLayout nameLayout = new HorizontalLayout();
        nameLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        Span nameLabel = new Span("Name : " + username);
        nameLabel.getStyle()
                .setFontWeight("bold")
                .setFontSize("1.1rem")
                .setColor("#000000");

        Icon editIcon1 = new Icon(VaadinIcon.PENCIL);
        editIcon1.setSize("18px");
        editIcon1.setColor("#000000");
        editIcon1.getStyle()
                .setCursor("pointer");

        editIcon1.addClickListener(e -> {
            showEditDialog("Edit Username", currentUser.getUsername(), newUsername -> {
                try {
                    UsersDAO dao = new UsersDAO();
                    dao.updateUser(
                            currentUser.getIdUsers(),
                            currentUser.getEmail(),
                            newUsername,
                            currentUser.getFoto()
                    );

                    currentUser.setUsername(newUsername);
                    nameLabel.setText("Name : " + newUsername);
                    Notification notif = new Notification(
                            "Username berhasil diperbarui", 3000, Notification.Position.MIDDLE
                    );
                    notif.open();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    Notification.show("Gagal menyimpan username", 3000, Notification.Position.TOP_CENTER);
                }
            });
        });

        nameLayout.add(nameLabel, editIcon1);

        HorizontalLayout emailLayout = new HorizontalLayout();
        emailLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        emailLayout.getStyle()
                .setMarginTop("20px");

        String emailText = currentUser != null ? currentUser.getEmail() : "email@example.com";
        Span emailLabel = new Span("Email : " + emailText);
        emailLabel.getStyle()
                .setFontWeight("bold")
                .setFontSize("1.1rem")
                .setColor("#000000");

        Icon editIcon2 = new Icon(VaadinIcon.PENCIL);
        editIcon2.setSize("18px");
        editIcon2.setColor("#000000");
        editIcon2.getStyle()
                .setCursor("pointer");

        editIcon2.addClickListener(e -> {
            showEditDialog("Edit Email", currentUser.getEmail(), newEmail -> {
                try {
                    UsersDAO dao = new UsersDAO();
                    dao.updateUser(
                            currentUser.getIdUsers(),
                            newEmail,
                            currentUser.getUsername(),
                            currentUser.getFoto()
                    );

                    currentUser.setEmail(newEmail);
                    emailLabel.setText("Email : " + newEmail);
                    Notification notif = new Notification(
                            "Email berhasil diperbarui", 3000, Notification.Position.MIDDLE
                    );
                    notif.open();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    Notification.show("Gagal menyimpan email", 3000, Notification.Position.TOP_CENTER);
                }
            });
        });

        emailLayout.add(emailLabel, editIcon2);

        profileContainer.add(nameLayout, emailLayout);

        VerticalLayout statsContainer = new VerticalLayout();
        statsContainer.addClassName("lora-text");
        statsContainer.setSpacing(false);
        statsContainer.setPadding(false);
        statsContainer.setAlignItems(FlexComponent.Alignment.END);

        HorizontalLayout bestScore = new HorizontalLayout();
        bestScore.setAlignItems(Alignment.CENTER);

        Span textBestScore = new Span("Best Score:");
        textBestScore.getStyle()
                .setFontWeight("bold")
                .setFontSize("1.1rem")
                .setColor("#000000");

        Span score = new Span("100");
        score.getStyle()
                .setFontWeight("bold")
                .setFontSize("1.1rem")
                .setColor("#118B50");

        bestScore.add(textBestScore, score);

        HorizontalLayout levelLayout = new HorizontalLayout();
        levelLayout.setAlignItems(Alignment.CENTER);
        levelLayout.getStyle()
                .setMarginTop("20px");

        Span textLevel = new Span("Level:");
        textLevel.getStyle()
                .setFontWeight("bold")
                .setFontSize("1.1rem")
                .setColor("#000000");

        Span level = new Span("100");
        level.getStyle()
                .setFontWeight("bold")
                .setFontSize("1.1rem")
                .setColor("#118B50");

        levelLayout.add(textLevel, level);

        statsContainer.add(bestScore, levelLayout);

        infoLayout.add(profileContainer, statsContainer);
        profileInfo.add(infoLayout);
        profileCard.add(avatarContainer, profileInfo);
        mainLayout.add(profileCard);

        return mainLayout;
    }

    private void showEditDialog(String title, String initialValue, Consumer<String> onSave) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(title);

        TextField field = new TextField();
        field.setValue(initialValue);
        field.setWidthFull();

        Button save = new Button("Simpan", ev -> {
            String newValue = field.getValue();
            onSave.accept(newValue);
            dialog.close();
        });
        save.getStyle().setColor("#16C47F");

        Button cancel = new Button("Batal", ev -> dialog.close());
        cancel.getStyle().setColor("red");

        HorizontalLayout actions = new HorizontalLayout(save, cancel);
        dialog.add(field, actions);
        dialog.open();
    }

    private Image createAvatarImage() {
        StreamResource resource = createImageStreamResource();
        Image img = new Image(resource, "User Avatar");
        img.setWidth("100%");
        img.setHeight("100%");
        img.getStyle()
                .setBorderRadius("50%")
                .set("object-fit", "cover");
        return img;
    }

    private StreamResource createImageStreamResource() {
        return new StreamResource("avatar.jpg", () -> {
            try {
                if (currentUser != null && currentUser.getFoto() != null && !currentUser.getFoto().isEmpty()) {
                    // Try to load user's photo
                    Path imagePath = Paths.get("src/main/resources/static/" + currentUser.getFoto());
                    if (Files.exists(imagePath)) {
                        return Files.newInputStream(imagePath);
                    }
                }
                // Fallback to default image
                Path defaultImagePath = Paths.get("src/main/resources/static/images/usericon.png");
                if (Files.exists(defaultImagePath)) {
                    return Files.newInputStream(defaultImagePath);
                }
                // If default image doesn't exist, create a simple placeholder
                return createPlaceholderImage();
            } catch (Exception e) {
                e.printStackTrace();
                return createPlaceholderImage();
            }
        });
    }

    private InputStream createPlaceholderImage() {
        // Create a simple 1x1 transparent PNG as fallback
        byte[] transparentPng = {
                (byte)0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, 0x00, 0x00, 0x00, 0x0D, 0x49, 0x48, 0x44, 0x52,
                0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01, 0x08, 0x06, 0x00, 0x00, 0x00, 0x1F, 0x15, (byte)0xC4,
                (byte)0x89, 0x00, 0x00, 0x00, 0x0A, 0x49, 0x44, 0x41, 0x54, 0x78, (byte)0x9C, 0x63, 0x00, 0x01, 0x00,
                0x00, 0x05, 0x00, 0x01, 0x0D, 0x0A, 0x2D, (byte)0xB4, 0x00, 0x00, 0x00, 0x00, 0x49, 0x45, 0x4E, 0x44,
                (byte)0xAE, 0x42, 0x60, (byte)0x82
        };
        return new ByteArrayInputStream(transparentPng);
    }

    private void setupUpload() {
        MemoryBuffer buffer = new MemoryBuffer();
        upload = new Upload(buffer);
        configureUploadComponent();

        upload.addSucceededListener(event -> handleUploadSuccess(event, buffer));
        upload.addFailedListener(event ->
                Notification.show("Upload gagal: " + event.getReason().getMessage(),
                        3000, Notification.Position.TOP_CENTER));
        upload.addFileRejectedListener(event ->
                Notification.show("File ditolak: " + event.getErrorMessage(),
                        3000, Notification.Position.TOP_CENTER));
    }

    private void configureUploadComponent() {
        upload.setAcceptedFileTypes("image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp");
        upload.setMaxFileSize(5 * 1024 * 1024); // 5MB
        upload.setAutoUpload(true);
        upload.getStyle().set("display", "none");
        upload.setDropLabel(new Span("Pilih foto avatar"));
    }

    private void handleUploadSuccess(SucceededEvent event, MemoryBuffer buffer) {
        if (currentUser == null) {
            Notification.show("Session user tidak valid. Silakan login ulang.",
                    3000, Notification.Position.TOP_CENTER);
            return;
        }

        try (InputStream fileStream = buffer.getInputStream()) {
            String originalFileName = event.getFileName();
            String extension = getFileExtension(originalFileName);

            if (!isValidImageExtension(extension)) {
                Notification.show("Format file tidak didukung. Gunakan JPG, PNG, GIF, atau WEBP.",
                        3000, Notification.Position.TOP_CENTER);
                return;
            }

            String savedFileName = UUID.randomUUID() + "_" + originalFileName;
            String relativePath = "uploads/" + savedFileName;
            Path targetPath = Paths.get("src/main/resources/static/uploads").resolve(savedFileName);

            // Create directory if not exists
            Files.createDirectories(targetPath.getParent());

            // Copy file to target location
            Files.copy(fileStream, targetPath, StandardCopyOption.REPLACE_EXISTING);

            // Update user avatar in database
            updateUserAvatar(relativePath);

        } catch (Exception e) {
            Notification.show("Gagal menyimpan foto ke server. Silakan coba lagi.",
                    3000, Notification.Position.TOP_CENTER);
            e.printStackTrace();
        }
    }

    private void updateUserAvatar(String relativePath) throws SQLException {
        UsersDAO userDAO = new UsersDAO();
        userDAO.updateUser(
                currentUser.getIdUsers(),
                currentUser.getEmail(),
                currentUser.getUsername(),
                relativePath
        );

        // Update current user object
        currentUser.setFoto(relativePath);

        // Update session
        VaadinSession.getCurrent().setAttribute(UsersModel.class, currentUser);

        // Refresh avatar image immediately by recreating the Image component
        refreshAvatarImage();

        Notification.show("Foto avatar berhasil diperbarui dan disimpan!",
                3000, Notification.Position.TOP_CENTER);
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName != null ? fileName.lastIndexOf('.') : -1;
        return (lastDotIndex == -1) ? "" : fileName.substring(lastDotIndex + 1).toLowerCase();
    }

    private boolean isValidImageExtension(String extension) {
        return List.of("jpg", "jpeg", "png", "gif", "webp").contains(extension);
    }

    private void refreshAvatarImage() {
        getUI().ifPresent(ui -> ui.access(() -> {
            // Get the parent container
            Component parent = avatar.getParent().orElse(null);
            if (parent instanceof Div) {
                Div avatarContainer = (Div) parent;

                // Remove old avatar
                avatarContainer.remove(avatar);

                // Create new avatar with updated image
                avatar = createAvatarImage();

                // Add new avatar (make sure it's added before plus button and upload)
                avatarContainer.addComponentAsFirst(avatar);

                // Push changes to client
                ui.push();
            }
        }));
    }
}