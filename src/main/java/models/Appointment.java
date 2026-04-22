package models;

// Classe représentant un rendez-vous médical
public class Appointment {

    private int id;
    private Patient patient;
    private Doctor doctor;
    private String date;
    private String time;

    // Constructeur
    public Appointment(int id, Patient patient, Doctor doctor, String date, String time) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    // Pour affichage dans TableView
    public String getPatientName() {
        return patient != null ? patient.getName() : "—";
    }

    public String getDoctorName() {
        return doctor != null ? doctor.toString() : "—";
    }

    @Override
    public String toString() {
        return "RDV: " + getPatientName() + " avec " + getDoctorName()
                + " le " + date + " à " + time;
    }
}