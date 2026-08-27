/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mainconsoleapp;

import java.util.*;

public class HospitalManager {
    private List<Patient> patients;
    private String[][] wardBeds; // 4 rows x 5 columns layout
    private Map<String, String> bedAllocations; // Bed ID -> Patient ID

    public HospitalManager() {
        patients = new ArrayList<>();
        wardBeds = new String[4][5];
        bedAllocations = new HashMap<>();
        initializeBeds();
    }

    private void initializeBeds() {
        int count = 1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                wardBeds[i][j] = String.format("B%02d", count++);
            }
        }
    }

    // Feature 1: Patient Management
    public boolean registerPatient(Patient patient) {
        if (searchPatient(patient.getPatientId()) != null) {
            return false; // Prevent duplicate Patient ID
        }
        patients.add(patient);
        return true;
    }

    public Patient searchPatient(String patientId) {
        for (Patient p : patients) {
            if (p.getPatientId().equalsIgnoreCase(patientId)) {
                return p;
            }
        }
        return null;
    }

    public boolean updatePatient(String patientId, String newFirstName, String newLastName, int newAge, String newCondition) {
        Patient p = searchPatient(patientId);
        if (p != null) {
            p.setFirstName(newFirstName);
            p.setLastName(newLastName);
            p.setAge(newAge);
            p.setMedicalCondition(newCondition);
            return true;
        }
        return false;
    }

    public boolean deletePatient(String patientId) {
        Patient p = searchPatient(patientId);
        if (p != null) {
            if (p instanceof Inpatient) {
                Inpatient inp = (Inpatient) p;
                if (inp.getBedNumber() != null) {
                    releaseBed(inp.getBedNumber());
                }
            }
            patients.remove(p);
            return true;
        }
        return false;
    }

    public List<Patient> getAllPatients() {
        return patients;
    }

    // Feature 2: Bed Management
    public boolean allocateBed(String patientId, String bedNumber) {
        Patient p = searchPatient(patientId);
        if (p == null || !(p instanceof Inpatient)) {
            return false; // Only inpatients can be allocated a bed
        }

        if (bedAllocations.containsKey(bedNumber)) {
            return false; // Bed is already occupied
        }

        if (isBedValid(bedNumber)) {
            Inpatient inpatient = (Inpatient) p;
            if (inpatient.getBedNumber() != null && !inpatient.getBedNumber().isEmpty()) {
                releaseBed(inpatient.getBedNumber()); // Release prior bed if assigned
            }
            inpatient.setBedNumber(bedNumber);
            inpatient.setWardNumber("Ward 1");
            bedAllocations.put(bedNumber, patientId);
            return true;
        }
        return false;
    }

    public boolean releaseBed(String bedNumber) {
        if (!bedAllocations.containsKey(bedNumber)) {
            return false; // Bed is not allocated
        }
        String patientId = bedAllocations.remove(bedNumber);
        Patient p = searchPatient(patientId);
        if (p instanceof Inpatient) {
            ((Inpatient) p).setBedNumber(null);
            ((Inpatient) p).setWardNumber(null);
        }
        return true;
    }

    private boolean isBedValid(String bedNumber) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (wardBeds[i][j].equalsIgnoreCase(bedNumber)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> getAvailableBeds() {
        List<String> available = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                String bed = wardBeds[i][j];
                if (!bedAllocations.containsKey(bed)) {
                    available.add(bed);
                }
            }
        }
        return available;
    }

    public List<String> getOccupiedBeds() {
        return new ArrayList<>(bedAllocations.keySet());
    }

    public void displayWardLayout() {
        System.out.println("\n--- Hospital Ward Layout (4x5) ---");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                String bed = wardBeds[i][j];
                if (bedAllocations.containsKey(bed)) {
                    System.out.print("[OCC] ");
                } else {
                    System.out.print("[" + bed + "] ");
                }
            }
            System.out.println();
        }
    }

    // Feature 3: Reports & Sorting
    public double getOccupancyPercentage() {
        return (getOccupiedBeds().size() / 20.0) * 100;
    }

    public void sortPatientsBySurname() {
        patients.sort(Comparator.comparing(Patient::getLastName));
    }

    public void sortPatientsById() {
        patients.sort(Comparator.comparing(Patient::getPatientId));
    }
}