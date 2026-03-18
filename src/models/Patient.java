package models;

// Classe représentant un patient — implémente les trois interfaces
import interfaces.Displayable;
import interfaces.Manageable;
import interfaces.Searchable;

import java.util.ArrayList;
import java.util.List;

public class Patient implements Manageable<Patient>, Displayable<Patient>, Searchable<Patient> {

    private int id;
    private String name;
    private int age;
    private String disease;
    private Room assignedRoom;

    // Liste interne pour les implémentations d'interface
    private List<Patient> patientList = new ArrayList<>();

    // Constructeur complet
    public Patient(int id, String name, int age, String disease, Room assignedRoom) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.disease = disease;
        this.assignedRoom = assignedRoom;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getDisease() { return disease; }
    public void setDisease(String disease) { this.disease = disease; }

    public Room getAssignedRoom() { return assignedRoom; }
    public void setAssignedRoom(Room assignedRoom) { this.assignedRoom = assignedRoom; }

    // Numéro de chambre pour affichage dans TableView
    public String getRoomInfo() {
        return assignedRoom != null ? "Chambre " + assignedRoom.getRoomNumber() : "Non assigné";
    }

    // --- Implémentation de Manageable ---
    @Override
    public void add(Patient patient) { patientList.add(patient); }

    @Override
    public void remove(Patient patient) { patientList.remove(patient); }

    @Override
    public void update(Patient oldPatient, Patient newPatient) {
        int index = patientList.indexOf(oldPatient);
        if (index >= 0) patientList.set(index, newPatient);
    }

    // --- Implémentation de Displayable ---
    @Override
    public List<Patient> display() { return new ArrayList<>(patientList); }

    // --- Implémentation de Searchable ---
    @Override
    public List<Patient> search(String keyword) {
        List<Patient> results = new ArrayList<>();
        String lower = keyword.toLowerCase();
        for (Patient p : patientList) {
            if (p.getName().toLowerCase().contains(lower)
                    || p.getDisease().toLowerCase().contains(lower)) {
                results.add(p);
            }
        }
        return results;
    }

    @Override
    public String toString() {
        return name + " (Age: " + age + ", " + disease + ")";
    }
}