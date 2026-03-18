package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import managers.DataStore;
import models.Appointment;
import models.Doctor;
import models.Patient;

public class AppointmentController {

    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, Integer> colApptId;
    @FXML private TableColumn<Appointment, String> colApptPatient;
    @FXML private TableColumn<Appointment, String> colApptDoctor;
    @FXML private TableColumn<Appointment, String> colApptDate;
    @FXML private TableColumn<Appointment, String> colApptTime;

    @FXML private ComboBox<Patient> comboApptPatient;
    @FXML private ComboBox<Doctor> comboApptDoctor;
    @FXML private TextField fieldDate;
    @FXML private TextField fieldTime;
    @FXML private Label labelStatus;
    @FXML private Label labelTotalAppt;
    @FXML private Label labelTodayAppt;

    private DataStore store = DataStore.getInstance();
    private Appointment selectedAppointment = null;

    @FXML
    public void initialize() {
        colApptId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colApptPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colApptDoctor.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        colApptDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colApptTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        comboApptPatient.setItems(store.getPatients());
        comboApptDoctor.setItems(store.getDoctors());
        appointmentTable.setItems(store.getAppointments());

        appointmentTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        selectedAppointment = newVal;
                        comboApptPatient.setValue(newVal.getPatient());
                        comboApptDoctor.setValue(newVal.getDoctor());
                        fieldDate.setText(newVal.getDate());
                        fieldTime.setText(newVal.getTime());
                    }
                }
        );

        // Fade-in
        AnimationManager.fadeIn(appointmentTable);

        updateStats();
        labelStatus.setText("✅ " + store.getTotalAppointments() + " rendez-vous chargés.");
    }

    @FXML
    public void handleAddAppointment() {
        if (comboApptPatient.getValue() == null ||
                comboApptDoctor.getValue() == null) {
            labelStatus.setText("⚠️ Patient et médecin obligatoires.");
            AnimationManager.shake(comboApptPatient);
            return;
        }
        if (fieldDate.getText().trim().isEmpty() ||
                fieldTime.getText().trim().isEmpty()) {
            labelStatus.setText("⚠️ Date et heure obligatoires.");
            AnimationManager.shake(fieldDate);
            return;
        }
        Appointment a = new Appointment(
                0,
                comboApptPatient.getValue(),
                comboApptDoctor.getValue(),
                fieldDate.getText().trim(),
                fieldTime.getText().trim()
        );
        store.addAppointment(a);
        updateStats();
        clearFields();
        labelStatus.setText("✅ Rendez-vous planifié.");
        AnimationManager.showNotification(
                appointmentTable.getScene().getWindow(),
                "✅ Rendez-vous planifié pour : " + a.getPatientName(), true);
    }

    @FXML
    public void handleDeleteAppointment() {
        if (selectedAppointment == null) {
            labelStatus.setText("⚠️ Sélectionnez un rendez-vous.");
            return;
        }
        String name = selectedAppointment.getPatientName();
        store.removeAppointment(selectedAppointment);
        updateStats();
        clearFields();
        labelStatus.setText("🗑️ Rendez-vous supprimé.");
        AnimationManager.showNotification(
                appointmentTable.getScene().getWindow(),
                "🗑️ Rendez-vous de " + name + " supprimé.", false);
    }

    @FXML
    public void handleUpdateAppointment() {
        if (selectedAppointment == null) {
            labelStatus.setText("⚠️ Sélectionnez un rendez-vous.");
            return;
        }
        if (fieldDate.getText().trim().isEmpty() ||
                fieldTime.getText().trim().isEmpty()) {
            labelStatus.setText("⚠️ Date et heure obligatoires.");
            AnimationManager.shake(fieldDate);
            return;
        }
        selectedAppointment.setPatient(comboApptPatient.getValue());
        selectedAppointment.setDoctor(comboApptDoctor.getValue());
        selectedAppointment.setDate(fieldDate.getText().trim());
        selectedAppointment.setTime(fieldTime.getText().trim());
        appointmentTable.refresh();
        updateStats();
        clearFields();
        labelStatus.setText("✏️ Rendez-vous modifié.");
        AnimationManager.showNotification(
                appointmentTable.getScene().getWindow(),
                "✏️ Rendez-vous modifié avec succès.", true);
    }

    @FXML
    public void handleClearAppointment() {
        clearFields();
        labelStatus.setText("🔄 Formulaire réinitialisé.");
    }

    private void clearFields() {
        comboApptPatient.setValue(null);
        comboApptDoctor.setValue(null);
        fieldDate.clear();
        fieldTime.clear();
        selectedAppointment = null;
        appointmentTable.getSelectionModel().clearSelection();
    }

    private void updateStats() {
        labelTotalAppt.setText(String.valueOf(store.getTotalAppointments()));
        labelTodayAppt.setText(String.valueOf(store.getTotalAppointments()));
    }
}