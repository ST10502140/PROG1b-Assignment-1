/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mainconsoleapp;

import java.util.Scanner;

public class MainConsoleApp {
    private static HospitalManager manager = new HospitalManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n===== MEDICARE HOSPITAL PATIENT ADMISSION SYSTEM =====");
            System.out.println("1. Patient Management");
            System.out.println("2. Bed Management");
            System.out.println("3. Reports & Analytics");
            System.out.println("4. Exit");
            System.out.print("Select an option: ");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    patientMenu();
                    break;
                case 2:
                    bedMenu();
                    break;
                case 3:
                    reportsMenu();
                    break;
                case 4:
                    exit = true;
                    System.out.println("Exiting system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void patientMenu() {
        System.out.println("\n--- Patient Management ---");
        System.out.println("1. Register New Patient");
        System.out.println("2. Search Patient");
        System.out.println("3. Update Patient Details");
        System.out.println("4. Delete Patient");
        System.out.println("5. Display All Patients");
        System.out.println("6. Sort Patients");
        System.out.print("Choice: ");

        int choice = getIntInput();
        switch (choice) {
            case 1:
                System.out.print("Enter Patient ID: ");
                String id = scanner.nextLine();
                System.out.print("Enter First Name: ");
                String fname = scanner.nextLine();
                System.out.print("Enter Last Name: ");
                String lname = scanner.nextLine();
                System.out.print("Enter Age: ");
                int age = getIntInput();
                System.out.print("Enter Gender: ");
                String gender = scanner.nextLine();
                System.out.print("Enter Medical Condition: ");
                String cond = scanner.nextLine();
                System.out.println("Category: 1. INPATIENT, 2. OUTPATIENT, 3. EMERGENCY");
                int catChoice = getIntInput();

                Patient p;
                if (catChoice == 1) {
                    p = new Inpatient(id, fname, lname, age, gender, cond, null, null);
                } else if (catChoice == 2) {
                    p = new Patient(id, fname, lname, age, gender, cond, PatientCategory.OUTPATIENT);
                } else {
                    p = new Patient(id, fname, lname, age, gender, cond, PatientCategory.EMERGENCY);
                }

                if (manager.registerPatient(p)) {
                    System.out.println("Patient registered successfully!");
                } else {
                    System.out.println("Error: Duplicate Patient ID.");
                }
                break;

            case 2:
                System.out.print("Enter Patient ID: ");
                Patient found = manager.searchPatient(scanner.nextLine());
                if (found != null) {
                    System.out.println(found.displayDetails());
                } else {
                    System.out.println("Patient not found.");
                }
                break;

            case 3:
                System.out.print("Enter Patient ID to update: ");
                String uid = scanner.nextLine();
                System.out.print("New First Name: ");
                String ufname = scanner.nextLine();
                System.out.print("New Last Name: ");
                String ulname = scanner.nextLine();
                System.out.print("New Age: ");
                int uage = getIntInput();
                System.out.print("New Condition: ");
                String ucond = scanner.nextLine();

                if (manager.updatePatient(uid, ufname, ulname, uage, ucond)) {
                    System.out.println("Patient details updated!");
                } else {
                    System.out.println("Patient not found.");
                }
                break;

            case 4:
                System.out.print("Enter Patient ID to delete: ");
                if (manager.deletePatient(scanner.nextLine())) {
                    System.out.println("Patient removed successfully!");
                } else {
                    System.out.println("Patient not found.");
                }
                break;

            case 5:
                System.out.println("\n--- All Patients ---");
                for (Patient patient : manager.getAllPatients()) {
                    System.out.println(patient.displayDetails());
                }
                break;

            case 6:
                System.out.println("Sort by: 1. Surname | 2. Patient ID");
                int sortChoice = getIntInput();
                if (sortChoice == 1) manager.sortPatientsBySurname();
                else manager.sortPatientsById();
                System.out.println("Patients sorted.");
                break;
        }
    }

    private static void bedMenu() {
        System.out.println("\n--- Bed Management ---");
        System.out.println("1. Display Ward Layout");
        System.out.println("2. Allocate Bed");
        System.out.println("3. Release Bed");
        System.out.println("4. Display Available Beds");
        System.out.println("5. Display Occupied Beds");
        System.out.print("Choice: ");

        int choice = getIntInput();
        switch (choice) {
            case 1:
                manager.displayWardLayout();
                break;
            case 2:
                if (manager.getAvailableBeds().isEmpty()) {
                    System.out.println("Cannot allocate bed: All beds are currently occupied!");
                    break;
                }
                System.out.print("Enter Inpatient ID: ");
                String pid = scanner.nextLine();
                System.out.print("Enter Bed Number (e.g., B01 - B20): ");
                String bed = scanner.nextLine().toUpperCase();
                if (manager.allocateBed(pid, bed)) {
                    System.out.println("Bed allocated successfully.");
                } else {
                    System.out.println("Allocation failed. Check if patient is an Inpatient and bed is valid/available.");
                }
                break;
            case 3:
                System.out.print("Enter Bed Number to release: ");
                String rBed = scanner.nextLine().toUpperCase();
                if (manager.releaseBed(rBed)) {
                    System.out.println("Bed released successfully.");
                } else {
                    System.out.println("Failed to release bed. Check bed number.");
                }
                break;
            case 4:
                System.out.println("Available Beds: " + manager.getAvailableBeds());
                break;
            case 5:
                System.out.println("Occupied Beds: " + manager.getOccupiedBeds());
                break;
        }
    }

    private static void reportsMenu() {
        System.out.println("\n--- Ward Reports ---");
        System.out.println("Registered Patients Count: " + manager.getAllPatients().size());
        System.out.println("Occupied Beds Count: " + manager.getOccupiedBeds().size());
        System.out.println("Available Beds Count: " + manager.getAvailableBeds().size());
        System.out.printf("Ward Occupancy: %.2f%%\n", manager.getOccupancyPercentage());
    }

    private static int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}