package managers;

import models.Appointment;
import models.Doctor;
import models.Patient;
import models.Room;

import java.io.*;
import java.nio.file.*;
import java.util.List;

// Gestionnaire de sauvegarde/chargement JSON
public class JsonManager {

    // Fichier de sauvegarde dans le dossier utilisateur
    private static final String SAVE_DIR = System.getProperty("user.home") + "/HospitalData/";
    private static final String PATIENTS_FILE = SAVE_DIR + "patients.json";
    private static final String DOCTORS_FILE = SAVE_DIR + "doctors.json";
    private static final String ROOMS_FILE = SAVE_DIR + "rooms.json";
    private static final String APPOINTMENTS_FILE = SAVE_DIR + "appointments.json";

    // Créer le dossier si n'existe pas
    public static void init() {
        try {
            Files.createDirectories(Paths.get(SAVE_DIR));
            System.out.println("✅ Save directory: " + SAVE_DIR);
        } catch (Exception e) {
            System.err.println("❌ Cannot create save directory: " + e.getMessage());
        }
    }

    // Vérifier si des données sauvegardées existent
    public static boolean hasSavedData() {
        return new File(PATIENTS_FILE).exists();
    }

    // ===== SAUVEGARDE =====

    public static void saveAll(DataStore store) {
        init();
        saveDoctors(store);
        saveRooms(store);
        savePatients(store);
        saveAppointments(store);
        System.out.println("✅ Toutes les données sauvegardées.");
    }

    private static void saveDoctors(DataStore store) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        var doctors = store.getDoctors();
        for (int i = 0; i < doctors.size(); i++) {
            var d = doctors.get(i);
            sb.append("  {\n");
            sb.append("    \"id\": ").append(d.getId()).append(",\n");
            sb.append("    \"name\": \"").append(escape(d.getName())).append("\",\n");
            sb.append("    \"specialization\": \"").append(escape(d.getSpecialization())).append("\",\n");
            sb.append("    \"phone\": \"").append(escape(d.getPhone())).append("\"\n");
            sb.append("  }").append(i < doctors.size() - 1 ? "," : "").append("\n");
        }
        sb.append("]");
        writeFile(DOCTORS_FILE, sb.toString());
    }

    private static void saveRooms(DataStore store) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        var rooms = store.getRooms();
        for (int i = 0; i < rooms.size(); i++) {
            var r = rooms.get(i);
            sb.append("  {\n");
            sb.append("    \"roomNumber\": ").append(r.getRoomNumber()).append(",\n");
            sb.append("    \"type\": \"").append(escape(r.getType())).append("\",\n");
            sb.append("    \"status\": \"").append(escape(r.getStatus())).append("\"\n");
            sb.append("  }").append(i < rooms.size() - 1 ? "," : "").append("\n");
        }
        sb.append("]");
        writeFile(ROOMS_FILE, sb.toString());
    }

    private static void savePatients(DataStore store) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        var patients = store.getPatients();
        for (int i = 0; i < patients.size(); i++) {
            var p = patients.get(i);
            sb.append("  {\n");
            sb.append("    \"id\": ").append(p.getId()).append(",\n");
            sb.append("    \"name\": \"").append(escape(p.getName())).append("\",\n");
            sb.append("    \"age\": ").append(p.getAge()).append(",\n");
            sb.append("    \"disease\": \"").append(escape(p.getDisease())).append("\",\n");
            sb.append("    \"roomNumber\": ").append(
                    p.getAssignedRoom() != null ? p.getAssignedRoom().getRoomNumber() : -1
            ).append("\n");
            sb.append("  }").append(i < patients.size() - 1 ? "," : "").append("\n");
        }
        sb.append("]");
        writeFile(PATIENTS_FILE, sb.toString());
    }

    private static void saveAppointments(DataStore store) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        var appts = store.getAppointments();
        for (int i = 0; i < appts.size(); i++) {
            var a = appts.get(i);
            sb.append("  {\n");
            sb.append("    \"id\": ").append(a.getId()).append(",\n");
            sb.append("    \"patientId\": ").append(
                    a.getPatient() != null ? a.getPatient().getId() : -1
            ).append(",\n");
            sb.append("    \"doctorId\": ").append(
                    a.getDoctor() != null ? a.getDoctor().getId() : -1
            ).append(",\n");
            sb.append("    \"date\": \"").append(escape(a.getDate())).append("\",\n");
            sb.append("    \"time\": \"").append(escape(a.getTime())).append("\"\n");
            sb.append("  }").append(i < appts.size() - 1 ? "," : "").append("\n");
        }
        sb.append("]");
        writeFile(APPOINTMENTS_FILE, sb.toString());
    }

    // ===== CHARGEMENT =====

    public static void loadAll(DataStore store) {
        if (!hasSavedData()) {
            System.out.println("ℹ️ Pas de données sauvegardées — données par défaut.");
            return;
        }
        store.clearAll();
        loadDoctors(store);
        loadRooms(store);
        loadPatients(store);
        loadAppointments(store);
        System.out.println("✅ Données chargées depuis fichier.");
    }

    private static void loadDoctors(DataStore store) {
        String content = readFile(DOCTORS_FILE);
        if (content == null) return;
        for (String block : splitBlocks(content)) {
            try {
                int id = parseInt(getValue(block, "id"));
                String name = getValue(block, "name");
                String spec = getValue(block, "specialization");
                String phone = getValue(block, "phone");
                Doctor d = new Doctor(id, name, spec, phone);
                store.getDoctors().add(d);
                store.updateDoctorCounter(id);
            } catch (Exception e) {
                System.err.println("Error loading doctor: " + e.getMessage());
            }
        }
    }

    private static void loadRooms(DataStore store) {
        String content = readFile(ROOMS_FILE);
        if (content == null) return;
        for (String block : splitBlocks(content)) {
            try {
                int number = parseInt(getValue(block, "roomNumber"));
                String type = getValue(block, "type");
                Room r = new Room(number, type);
                store.getRooms().add(r);
            } catch (Exception e) {
                System.err.println("Error loading room: " + e.getMessage());
            }
        }
    }

    private static void loadPatients(DataStore store) {
        String content = readFile(PATIENTS_FILE);
        if (content == null) return;
        for (String block : splitBlocks(content)) {
            try {
                int id = parseInt(getValue(block, "id"));
                String name = getValue(block, "name");
                int age = parseInt(getValue(block, "age"));
                String disease = getValue(block, "disease");
                int roomNumber = parseInt(getValue(block, "roomNumber"));

                // Trouver la chambre
                Room room = null;
                if (roomNumber != -1) {
                    for (Room r : store.getRooms()) {
                        if (r.getRoomNumber() == roomNumber) {
                            room = r;
                            break;
                        }
                    }
                }

                Patient p = new Patient(id, name, age, disease, room);
                if (room != null) room.setAssignedPatient(p);
                store.getPatients().add(p);
                store.updatePatientCounter(id);
            } catch (Exception e) {
                System.err.println("Error loading patient: " + e.getMessage());
            }
        }
    }

    private static void loadAppointments(DataStore store) {
        String content = readFile(APPOINTMENTS_FILE);
        if (content == null) return;
        for (String block : splitBlocks(content)) {
            try {
                int id = parseInt(getValue(block, "id"));
                int patientId = parseInt(getValue(block, "patientId"));
                int doctorId = parseInt(getValue(block, "doctorId"));
                String date = getValue(block, "date");
                String time = getValue(block, "time");

                // Trouver patient et médecin
                Patient patient = null;
                Doctor doctor = null;
                for (Patient p : store.getPatients()) {
                    if (p.getId() == patientId) { patient = p; break; }
                }
                for (Doctor d : store.getDoctors()) {
                    if (d.getId() == doctorId) { doctor = d; break; }
                }

                Appointment a = new Appointment(id, patient, doctor, date, time);
                store.getAppointments().add(a);
                store.updateAppointmentCounter(id);
            } catch (Exception e) {
                System.err.println("Error loading appointment: " + e.getMessage());
            }
        }
    }

    // ===== UTILITAIRES =====

    private static void writeFile(String path, String content) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.print(content);
        } catch (Exception e) {
            System.err.println("❌ Cannot write file: " + path);
        }
    }

    private static String readFile(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (Exception e) {
            return null;
        }
    }

    private static List<String> splitBlocks(String json) {
        List<String> blocks = new java.util.ArrayList<>();
        int depth = 0;
        int start = -1;
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') {
                if (depth == 0) start = i;
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && start != -1) {
                    blocks.add(json.substring(start, i + 1));
                }
            }
        }
        return blocks;
    }

    private static String getValue(String block, String key) {
        String search = "\"" + key + "\":";
        int idx = block.indexOf(search);
        if (idx == -1) return "";
        int start = idx + search.length();
        while (start < block.length() && block.charAt(start) == ' ') start++;
        if (block.charAt(start) == '"') {
            int end = block.indexOf('"', start + 1);
            return block.substring(start + 1, end);
        } else {
            int end = start;
            while (end < block.length() &&
                    block.charAt(end) != ',' &&
                    block.charAt(end) != '\n' &&
                    block.charAt(end) != '}') end++;
            return block.substring(start, end).trim();
        }
    }

    private static int parseInt(String s) {
        try { return Integer.parseInt(s.trim()); }
        catch (Exception e) { return -1; }
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}