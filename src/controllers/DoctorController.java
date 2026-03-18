package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import managers.DataStore;
import models.Doctor;

public class DoctorController {

    @FXML private TableView<Doctor> doctorTable;
    @FXML private TableColumn<Doctor, Integer> colDoctorId;
    @FXML private TableColumn<Doctor, String> colDoctorName;
    @FXML private TableColumn<Doctor, String> colSpecialization;
    @FXML private TableColumn<Doctor, String> colPhone;

    @FXML private TextField fieldDoctorName;
    @FXML private TextField fieldSpecialization;
    @FXML private TextField fieldPhone;
    @FXML private Label labelStatus;

    private DataStore store = DataStore.getInstance();
    private Doctor selectedDoctor = null;

    @FXML
    public void initialize() {
        colDoctorId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDoctorName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        doctorTable.setItems(store.getDoctors());

        doctorTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        selectedDoctor = newVal;
                        fieldDoctorName.setText(newVal.getName());
                        fieldSpecialization.setText(newVal.getSpecialization());
                        fieldPhone.setText(newVal.getPhone());
                    }
                }
        );

        // Hover effects
        AnimationManager.addHoverEffect(doctorTable);

        // Fade-in
        AnimationManager.fadeIn(doctorTable);

        labelStatus.setText("✅ " + store.getTotalDoctors() + " médecins chargés.");
    }

    @FXML
    public void handleAddDoctor() {
        if (fieldDoctorName.getText().trim().isEmpty() ||
                fieldSpecialization.getText().trim().isEmpty()) {
            labelStatus.setText("⚠️ Nom et spécialisation obligatoires.");
            AnimationManager.shake(fieldDoctorName);
            return;
        }
        Doctor d = new Doctor(0,
                fieldDoctorName.getText().trim(),
                fieldSpecialization.getText().trim(),
                fieldPhone.getText().trim());
        store.addDoctor(d);
        clearFields();
        labelStatus.setText("✅ Médecin ajouté : " + d.getName());
        AnimationManager.showNotification(
                doctorTable.getScene().getWindow(),
                "✅ Médecin ajouté : " + d.getName(), true);
    }

    @FXML
    public void handleUpdateDoctor() {
        if (selectedDoctor == null) {
            labelStatus.setText("⚠️ Sélectionnez un médecin à modifier.");
            return;
        }
        Doctor updated = new Doctor(0,
                fieldDoctorName.getText().trim(),
                fieldSpecialization.getText().trim(),
                fieldPhone.getText().trim());
        store.updateDoctor(selectedDoctor, updated);
        clearFields();
        labelStatus.setText("✏️ Médecin modifié.");
        AnimationManager.showNotification(
                doctorTable.getScene().getWindow(),
                "✏️ Médecin modifié avec succès.", true);
    }

    @FXML
    public void handleDeleteDoctor() {
        if (selectedDoctor == null) {
            labelStatus.setText("⚠️ Sélectionnez un médecin à supprimer.");
            return;
        }
        String name = selectedDoctor.getName();
        store.removeDoctor(selectedDoctor);
        clearFields();
        labelStatus.setText("🗑️ Médecin supprimé.");
        AnimationManager.showNotification(
                doctorTable.getScene().getWindow(),
                "🗑️ Médecin supprimé : " + name, false);
    }

    @FXML
    public void handleClearDoctor() {
        clearFields();
        labelStatus.setText("🔄 Formulaire réinitialisé.");
    }

    private void clearFields() {
        fieldDoctorName.clear();
        fieldSpecialization.clear();
        fieldPhone.clear();
        selectedDoctor = null;
        doctorTable.getSelectionModel().clearSelection();
    }
}