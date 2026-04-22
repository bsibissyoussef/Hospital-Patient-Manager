package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import managers.DataStore;
import models.Patient;
import models.Room;

public class MainController {

    @FXML private BorderPane mainLayout;
    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, Integer> colId;
    @FXML private TableColumn<Patient, String> colName;
    @FXML private TableColumn<Patient, Integer> colAge;
    @FXML private TableColumn<Patient, String> colDisease;
    @FXML private TableColumn<Patient, String> colRoom;
    @FXML private TextField fieldName;
    @FXML private TextField fieldAge;
    @FXML private TextField fieldDisease;
    @FXML private ComboBox<Room> comboRooms;
    @FXML private TextField fieldSearch;
    @FXML private Label menuPatients;
    @FXML private Label menuDoctors;
    @FXML private Label menuRooms;
    @FXML private Label menuAppointments;
    @FXML private Label menuStats;
    @FXML private Label labelStatus;
    @FXML private Button btnTheme;

    private DataStore store = DataStore.getInstance();
    private Patient selectedPatient = null;
    private Node patientPage = null;

    @FXML
    public void initialize() {
        // Configurer colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colDisease.setCellValueFactory(new PropertyValueFactory<>("disease"));
        colRoom.setCellValueFactory(new PropertyValueFactory<>("roomInfo"));

        // Lier données
        patientTable.setItems(store.getPatients());
        refreshRoomCombo();

        // Sélection table
        patientTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> fillForm(newVal)
        );

        labelStatus.setText("✅ Application démarrée avec succès.");
        patientPage = mainLayout.getCenter();

        // Hover effects sur sidebar
        AnimationManager.addHoverEffect(menuPatients);
        AnimationManager.addHoverEffect(menuDoctors);
        AnimationManager.addHoverEffect(menuRooms);
        AnimationManager.addHoverEffect(menuAppointments);
        AnimationManager.addHoverEffect(menuStats);

        // Fade-in page au démarrage
        AnimationManager.fadeIn(patientPage);
    }

    // =========================================================
    // NAVIGATION
    // =========================================================

    @FXML
    public void showPatients() {
        setActiveMenu(menuPatients);
        mainLayout.setCenter(patientPage);
        AnimationManager.slideIn(patientPage);
        patientTable.setItems(store.getPatients());
        refreshRoomCombo();
        labelStatus.setText("👥 Page Patients");
    }

    @FXML
    public void showDoctors() {
        setActiveMenu(menuDoctors);
        loadPage("doctors.fxml");
    }

    @FXML
    public void showRooms() {
        setActiveMenu(menuRooms);
        loadPage("rooms.fxml");
    }

    @FXML
    public void showAppointments() {
        setActiveMenu(menuAppointments);
        loadPage("appointments.fxml");
    }

    @FXML
    public void showStats() {
        setActiveMenu(menuStats);
        loadPage("stats.fxml");
    }

    private void loadPage(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/resources/" + fxml));
            Node page = loader.load();
            AnimationManager.slideIn(page);
            mainLayout.setCenter(page);
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setActiveMenu(Label active) {
        Label[] menus = {menuPatients, menuDoctors, menuRooms,
                menuAppointments, menuStats};
        for (Label m : menus) {
            m.getStyleClass().remove("menu-item-active");
            if (!m.getStyleClass().contains("menu-item")) {
                m.getStyleClass().add("menu-item");
            }
        }
        active.getStyleClass().remove("menu-item");
        if (!active.getStyleClass().contains("menu-item-active")) {
            active.getStyleClass().add("menu-item-active");
        }
    }

    // =========================================================
    // DARK MODE
    // =========================================================

    @FXML
    public void toggleTheme() {
        ThemeManager.toggle(mainLayout.getScene());
        if (ThemeManager.isDark()) {
            btnTheme.setText("☀️  Light Mode");
            btnTheme.setStyle(
                    "-fx-background-color: #FFA000;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 13px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8;" +
                            "-fx-padding: 10 0 10 0;" +
                            "-fx-cursor: hand;");
        } else {
            btnTheme.setText("🌙  Dark Mode");
            btnTheme.setStyle(
                    "-fx-background-color: #283593;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 13px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 8;" +
                            "-fx-padding: 10 0 10 0;" +
                            "-fx-cursor: hand;");
        }
        AnimationManager.showNotification(
                mainLayout.getScene().getWindow(),
                ThemeManager.isDark() ? "🌙 Dark Mode activé" : "☀️ Light Mode activé",
                true);
    }

    // =========================================================
    // CRUD PATIENTS
    // =========================================================

    @FXML
    public void handleAdd() {
        if (!validateForm()) {
            AnimationManager.shake(fieldName);
            return;
        }
        Patient p = new Patient(
                0,
                fieldName.getText().trim(),
                Integer.parseInt(fieldAge.getText().trim()),
                fieldDisease.getText().trim(),
                comboRooms.getValue()
        );
        store.addPatient(p);
        refreshRoomCombo();
        clearForm();
        labelStatus.setText("✅ Patient ajouté : " + p.getName());
        AnimationManager.showNotification(
                patientTable.getScene().getWindow(),
                "✅ Patient ajouté : " + p.getName(), true);
    }

    @FXML
    public void handleDelete() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            labelStatus.setText("⚠️ Sélectionnez un patient.");
            return;
        }
        store.removePatient(selected);
        refreshRoomCombo();
        clearForm();
        labelStatus.setText("🗑️ Patient supprimé : " + selected.getName());
        AnimationManager.showNotification(
                patientTable.getScene().getWindow(),
                "🗑️ Patient supprimé : " + selected.getName(), false);
    }

    @FXML
    public void handleUpdate() {
        if (selectedPatient == null) {
            labelStatus.setText("⚠️ Sélectionnez un patient à modifier.");
            return;
        }
        if (!validateForm()) {
            AnimationManager.shake(fieldName);
            return;
        }
        Patient updated = new Patient(
                0,
                fieldName.getText().trim(),
                Integer.parseInt(fieldAge.getText().trim()),
                fieldDisease.getText().trim(),
                comboRooms.getValue()
        );
        store.updatePatient(selectedPatient, updated);
        refreshRoomCombo();
        clearForm();
        labelStatus.setText("✏️ Patient modifié : " + updated.getName());
        AnimationManager.showNotification(
                patientTable.getScene().getWindow(),
                "✏️ Patient modifié : " + updated.getName(), true);
    }

    @FXML
    public void handleSearch() {
        String keyword = fieldSearch.getText().trim();
        if (keyword.isEmpty()) {
            patientTable.setItems(store.getPatients());
            labelStatus.setText("🔍 Tous les patients affichés.");
            return;
        }
        ObservableList<Patient> results = FXCollections.observableArrayList();
        for (Patient p : store.getPatients()) {
            if (p.getName().toLowerCase().contains(keyword.toLowerCase())
                    || p.getDisease().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(p);
            }
        }
        patientTable.setItems(results);
        labelStatus.setText("🔍 " + results.size() + " résultat(s) pour : " + keyword);
    }

    @FXML
    public void handleClear() {
        clearForm();
        patientTable.setItems(store.getPatients());
        labelStatus.setText("🔄 Formulaire réinitialisé.");
    }

    // =========================================================
    // UTILITAIRES
    // =========================================================

    private void fillForm(Patient p) {
        if (p != null) {
            selectedPatient = p;
            fieldName.setText(p.getName());
            fieldAge.setText(String.valueOf(p.getAge()));
            fieldDisease.setText(p.getDisease());
            comboRooms.setValue(p.getAssignedRoom());
        }
    }

    private void clearForm() {
        fieldName.clear();
        fieldAge.clear();
        fieldDisease.clear();
        comboRooms.setValue(null);
        fieldSearch.clear();
        selectedPatient = null;
        patientTable.getSelectionModel().clearSelection();
    }

    private void refreshRoomCombo() {
        Room current = comboRooms.getValue();
        comboRooms.setItems(store.getAvailableRooms());
        comboRooms.setValue(current);
    }

    private boolean validateForm() {
        if (fieldName.getText().trim().isEmpty()) {
            labelStatus.setText("⚠️ Le nom est obligatoire.");
            return false;
        }
        try {
            int age = Integer.parseInt(fieldAge.getText().trim());
            if (age <= 0 || age > 120) {
                labelStatus.setText("⚠️ L'âge doit être entre 1 et 120.");
                return false;
            }
        } catch (NumberFormatException e) {
            labelStatus.setText("⚠️ L'âge doit être un nombre valide.");
            return false;
        }
        if (fieldDisease.getText().trim().isEmpty()) {
            labelStatus.setText("⚠️ La maladie est obligatoire.");
            return false;
        }
        return true;
    }
}