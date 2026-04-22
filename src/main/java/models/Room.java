package models;

// Classe représentant une chambre hospitalière
public class Room {

    private int roomNumber;
    private String type;
    private String status; // "Disponible" ou "Occupée"
    private Patient assignedPatient; // null si disponible

    // Constructeur
    public Room(int roomNumber, String type) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.status = "Disponible";
        this.assignedPatient = null;
    }

    // Getters et Setters
    public int getRoomNumber() { return roomNumber; }
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Patient getAssignedPatient() { return assignedPatient; }
    public void setAssignedPatient(Patient assignedPatient) {
        this.assignedPatient = assignedPatient;
        // Mettre à jour le statut automatiquement
        this.status = (assignedPatient != null) ? "Occupée" : "Disponible";
    }

    // Nom du patient assigné pour la TableView
    public String getAssignedPatientName() {
        return assignedPatient != null ? assignedPatient.getName() : "—";
    }

    // Vérifier si la chambre est disponible
    public boolean isAvailable() {
        return "Disponible".equals(this.status);
    }

    @Override
    public String toString() {
        return "Chambre " + roomNumber + " (" + type + ") — " + status;
    }
}