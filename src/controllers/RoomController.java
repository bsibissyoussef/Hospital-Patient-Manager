package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import managers.DataStore;
import models.Patient;
import models.Room;

public class RoomController {

    @FXML private TableView<Room> roomTable;
    @FXML private TableColumn<Room, Integer> colRoomNumber;
    @FXML private TableColumn<Room, String> colRoomType;
    @FXML private TableColumn<Room, String> colRoomStatus;
    @FXML private TableColumn<Room, String> colRoomPatient;

    @FXML private TextField fieldRoomNumber;
    @FXML private ComboBox<String> comboRoomType;
    @FXML private ComboBox<Patient> comboPatient;
    @FXML private Label labelStatus;
    @FXML private Label labelAvailable;
    @FXML private Label labelOccupied;
    @FXML private Label labelTotal;

    private DataStore store = DataStore.getInstance();
    private Room selectedRoom = null;

    @FXML
    public void initialize() {
        colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        colRoomType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colRoomStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colRoomPatient.setCellValueFactory(new PropertyValueFactory<>("assignedPatientName"));

        // Colorer les lignes
        roomTable.setRowFactory(tv -> new TableRow<Room>() {
            @Override
            protected void updateItem(Room room, boolean empty) {
                super.updateItem(room, empty);
                if (room == null || empty) {
                    setStyle("");
                } else if ("Occupée".equals(room.getStatus())) {
                    setStyle("-fx-background-color: #FFEBEE;");
                } else {
                    setStyle("-fx-background-color: #E8F5E9;");
                }
            }
        });

        comboRoomType.getItems().addAll("Simple", "Double", "VIP", "Urgence");
        comboPatient.setItems(store.getPatients());
        roomTable.setItems(store.getRooms());

        roomTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        selectedRoom = newVal;
                        fieldRoomNumber.setText(String.valueOf(newVal.getRoomNumber()));
                        comboRoomType.setValue(newVal.getType());
                        comboPatient.setValue(newVal.getAssignedPatient());
                    }
                }
        );

        // Fade-in
        AnimationManager.fadeIn(roomTable);

        updateStats();
        labelStatus.setText("✅ " + store.getTotalRooms() + " chambres chargées.");
    }

    @FXML
    public void handleAddRoom() {
        if (fieldRoomNumber.getText().trim().isEmpty() ||
                comboRoomType.getValue() == null) {
            labelStatus.setText("⚠️ Numéro et type obligatoires.");
            AnimationManager.shake(fieldRoomNumber);
            return;
        }
        try {
            int number = Integer.parseInt(fieldRoomNumber.getText().trim());
            Room r = new Room(number, comboRoomType.getValue());
            store.addRoom(r);
            updateStats();
            clearFields();
            labelStatus.setText("✅ Chambre " + number + " ajoutée.");
            AnimationManager.showNotification(
                    roomTable.getScene().getWindow(),
                    "✅ Chambre " + number + " ajoutée.", true);
        } catch (NumberFormatException e) {
            labelStatus.setText("⚠️ Le numéro doit être un nombre.");
            AnimationManager.shake(fieldRoomNumber);
        }
    }

    @FXML
    public void handleAssignPatient() {
        if (selectedRoom == null) {
            labelStatus.setText("⚠️ Sélectionnez une chambre.");
            return;
        }
        if (!selectedRoom.isAvailable()) {
            labelStatus.setText("⚠️ Chambre déjà occupée.");
            AnimationManager.shake(roomTable);
            return;
        }
        Patient p = comboPatient.getValue();
        if (p == null) {
            labelStatus.setText("⚠️ Sélectionnez un patient.");
            return;
        }
        if (p.getAssignedRoom() != null) {
            p.getAssignedRoom().setAssignedPatient(null);
        }
        selectedRoom.setAssignedPatient(p);
        p.setAssignedRoom(selectedRoom);
        roomTable.refresh();
        updateStats();
        clearFields();
        labelStatus.setText("✅ " + p.getName() + " assigné.");
        AnimationManager.showNotification(
                roomTable.getScene().getWindow(),
                "✅ " + p.getName() + " assigné à Chambre "
                        + selectedRoom.getRoomNumber(), true);
    }

    @FXML
    public void handleReleaseRoom() {
        if (selectedRoom == null) {
            labelStatus.setText("⚠️ Sélectionnez une chambre.");
            return;
        }
        if (selectedRoom.isAvailable()) {
            labelStatus.setText("⚠️ La chambre est déjà disponible.");
            return;
        }
        Patient p = selectedRoom.getAssignedPatient();
        selectedRoom.setAssignedPatient(null);
        if (p != null) p.setAssignedRoom(null);
        roomTable.refresh();
        updateStats();
        clearFields();
        labelStatus.setText("✅ Chambre " + selectedRoom.getRoomNumber() + " libérée.");
        AnimationManager.showNotification(
                roomTable.getScene().getWindow(),
                "🔓 Chambre " + selectedRoom.getRoomNumber() + " libérée.", true);
    }

    @FXML
    public void handleDeleteRoom() {
        if (selectedRoom == null) {
            labelStatus.setText("⚠️ Sélectionnez une chambre.");
            return;
        }
        int number = selectedRoom.getRoomNumber();
        store.removeRoom(selectedRoom);
        updateStats();
        clearFields();
        labelStatus.setText("🗑️ Chambre supprimée.");
        AnimationManager.showNotification(
                roomTable.getScene().getWindow(),
                "🗑️ Chambre " + number + " supprimée.", false);
    }

    @FXML
    public void handleClearRoom() {
        clearFields();
        labelStatus.setText("🔄 Formulaire réinitialisé.");
    }

    private void clearFields() {
        fieldRoomNumber.clear();
        comboRoomType.setValue(null);
        comboPatient.setValue(null);
        selectedRoom = null;
        roomTable.getSelectionModel().clearSelection();
    }

    private void updateStats() {
        int total = store.getTotalRooms();
        int available = store.getAvailableRoomsCount();
        int occupied = total - available;
        labelTotal.setText(String.valueOf(total));
        labelAvailable.setText(String.valueOf(available));
        labelOccupied.setText(String.valueOf(occupied));
        // Pulse animation sur stats
        AnimationManager.pulse(labelTotal);
    }
}