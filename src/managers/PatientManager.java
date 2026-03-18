package managers;

import interfaces.Displayable;
import interfaces.Manageable;
import interfaces.Searchable;
import models.Patient;

import java.util.ArrayList;
import java.util.List;

// Gestionnaire central des patients — implémente les trois interfaces
public class PatientManager implements Manageable<Patient>, Displayable<Patient>, Searchable<Patient> {

    // Liste principale des patients
    private List<Patient> patients = new ArrayList<>();

    // Compteur automatique d'ID
    private int idCounter = 1;

    // --- Implémentation de Manageable ---

    @Override
    public void add(Patient patient) {
        patient.setId(idCounter++);
        patients.add(patient);
    }

    @Override
    public void remove(Patient patient) {
        patients.remove(patient);
    }

    @Override
    public void update(Patient oldPatient, Patient newPatient) {
        int index = patients.indexOf(oldPatient);
        if (index >= 0) {
            newPatient.setId(oldPatient.getId());
            patients.set(index, newPatient);
        }
    }

    // --- Implémentation de Displayable ---

    @Override
    public List<Patient> display() {
        return new ArrayList<>(patients);
    }

    // --- Implémentation de Searchable ---

    @Override
    public List<Patient> search(String keyword) {
        // Rechercher par nom ou maladie
        List<Patient> results = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Patient p : patients) {
            if (p.getName().toLowerCase().contains(lowerKeyword)
                    || p.getDisease().toLowerCase().contains(lowerKeyword)) {
                results.add(p);
            }
        }
        return results;
    }

    // Retourner le nombre total de patients
    public int getCount() {
        return patients.size();
    }
}