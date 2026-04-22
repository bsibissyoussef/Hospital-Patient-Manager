package managers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Appointment;
import models.Doctor;
import models.Patient;
import models.Room;

// Classe centrale — partage les données entre tous les contrôleurs
public class DataStore {

    // Instance unique (Singleton)
    private static DataStore instance;

    // Listes partagées
    private ObservableList<Patient> patients = FXCollections.observableArrayList();
    private ObservableList<Doctor> doctors = FXCollections.observableArrayList();
    private ObservableList<Room> rooms = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    // Compteurs d'ID
    private int patientIdCounter = 1;
    private int doctorIdCounter = 1;
    private int appointmentIdCounter = 1;

    private DataStore() {}

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    // Initialiser — charger depuis fichier ou données par défaut
    public void init() {
        if (JsonManager.hasSavedData()) {
            JsonManager.loadAll(this);
            System.out.println("✅ Données chargées depuis fichier.");
        } else {
            loadDefaultData();
            System.out.println("ℹ️ Données par défaut chargées.");
        }
    }

    // Données par défaut
    private void loadDefaultData() {
        doctors.addAll(
                new Doctor(doctorIdCounter++, "Dr. Martin", "Cardiologie", "0612345678"),
                new Doctor(doctorIdCounter++, "Dr. Dupont", "Neurologie", "0623456789"),
                new Doctor(doctorIdCounter++, "Dr. Lemaire", "Pédiatrie", "0634567890"),
                new Doctor(doctorIdCounter++, "Dr. Bernard", "Chirurgie", "0645678901"),
                new Doctor(doctorIdCounter++, "Dr. Rousseau", "Dermatologie", "0656789012")
        );

        rooms.addAll(
                new Room(101, "Simple"),
                new Room(102, "Simple"),
                new Room(103, "Double"),
                new Room(104, "Double"),
                new Room(105, "VIP"),
                new Room(106, "VIP"),
                new Room(107, "Urgence"),
                new Room(108, "Urgence")
        );

        addPatient(new Patient(0, "Ahmed Benali", 45, "Hypertension", rooms.get(0)));
        addPatient(new Patient(0, "Fatima Zahra", 32, "Diabète", rooms.get(1)));
        addPatient(new Patient(0, "Youssef Kadiri", 67, "Insuffisance cardiaque", null));
        addPatient(new Patient(0, "Nadia Tazi", 28, "Migraine", null));

        appointments.addAll(
                new Appointment(appointmentIdCounter++, patients.get(0), doctors.get(0), "20/03/2026", "09:00"),
                new Appointment(appointmentIdCounter++, patients.get(1), doctors.get(1), "21/03/2026", "10:30"),
                new Appointment(appointmentIdCounter++, patients.get(2), doctors.get(0), "22/03/2026", "14:00")
        );
    }

    // Vider toutes les listes pour le chargement
    public void clearAll() {
        patients.clear();
        doctors.clear();
        rooms.clear();
        appointments.clear();
        patientIdCounter = 1;
        doctorIdCounter = 1;
        appointmentIdCounter = 1;
    }

    // Mettre à jour les compteurs lors du chargement
    public void updatePatientCounter(int id) {
        if (id >= patientIdCounter) patientIdCounter = id + 1;
    }

    public void updateDoctorCounter(int id) {
        if (id >= doctorIdCounter) doctorIdCounter = id + 1;
    }

    public void updateAppointmentCounter(int id) {
        if (id >= appointmentIdCounter) appointmentIdCounter = id + 1;
    }

    // --- Patients ---
    public ObservableList<Patient> getPatients() { return patients; }

    public void addPatient(Patient p) {
        p.setId(patientIdCounter++);
        if (p.getAssignedRoom() != null) {
            p.getAssignedRoom().setAssignedPatient(p);
        }
        patients.add(p);
        autoSave();
    }

    public void removePatient(Patient p) {
        if (p.getAssignedRoom() != null) {
            p.getAssignedRoom().setAssignedPatient(null);
        }
        patients.remove(p);
        autoSave();
    }

    public void updatePatient(Patient old, Patient updated) {
        int index = patients.indexOf(old);
        if (index >= 0) {
            updated.setId(old.getId());
            if (old.getAssignedRoom() != null) {
                old.getAssignedRoom().setAssignedPatient(null);
            }
            if (updated.getAssignedRoom() != null) {
                updated.getAssignedRoom().setAssignedPatient(updated);
            }
            patients.set(index, updated);
        }
        autoSave();
    }

    // --- Doctors ---
    public ObservableList<Doctor> getDoctors() { return doctors; }

    public void addDoctor(Doctor d) {
        d.setId(doctorIdCounter++);
        doctors.add(d);
        autoSave();
    }

    public void removeDoctor(Doctor d) {
        doctors.remove(d);
        autoSave();
    }

    public void updateDoctor(Doctor old, Doctor updated) {
        int index = doctors.indexOf(old);
        if (index >= 0) {
            updated.setId(old.getId());
            doctors.set(index, updated);
        }
        autoSave();
    }

    // --- Rooms ---
    public ObservableList<Room> getRooms() { return rooms; }

    public ObservableList<Room> getAvailableRooms() {
        ObservableList<Room> available = FXCollections.observableArrayList();
        for (Room r : rooms) {
            if (r.isAvailable()) available.add(r);
        }
        return available;
    }

    public void addRoom(Room r) {
        rooms.add(r);
        autoSave();
    }

    public void removeRoom(Room r) {
        rooms.remove(r);
        autoSave();
    }

    // --- Appointments ---
    public ObservableList<Appointment> getAppointments() { return appointments; }

    public void addAppointment(Appointment a) {
        a.setId(appointmentIdCounter++);
        appointments.add(a);
        autoSave();
    }

    public void removeAppointment(Appointment a) {
        appointments.remove(a);
        autoSave();
    }

    // --- Statistiques ---
    public int getTotalPatients() { return patients.size(); }
    public int getTotalDoctors() { return doctors.size(); }
    public int getTotalRooms() { return rooms.size(); }
    public int getAvailableRoomsCount() {
        return (int) rooms.stream().filter(Room::isAvailable).count();
    }
    public int getTotalAppointments() { return appointments.size(); }

    // Auto-save après chaque modification
    private void autoSave() {
        JsonManager.saveAll(this);
        System.out.println("💾 Auto-saved.");
    }
}