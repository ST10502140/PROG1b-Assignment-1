/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.mainconsoleapp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HospitalManagerTest {
    private HospitalManager manager;

    @BeforeEach
    public void setUp() {
        manager = new HospitalManager();
    }

    @Test
    public void testRegisterPatient() {
        Patient p = new Patient("P101", "John", "Doe", 30, "Male", "Flu", PatientCategory.OUTPATIENT);
        assertTrue(manager.registerPatient(p));
        assertEquals(1, manager.getAllPatients().size());
    }

    @Test
    public void testPreventDuplicatePatientId() {
        Patient p1 = new Patient("P101", "John", "Doe", 30, "Male", "Flu", PatientCategory.OUTPATIENT);
        Patient p2 = new Patient("P101", "Jane", "Smith", 25, "Female", "Fever", PatientCategory.OUTPATIENT);
        
        manager.registerPatient(p1);
        assertFalse(manager.registerPatient(p2));
    }

    @Test
    public void testSearchPatient() {
        Patient p = new Patient("P102", "Alice", "Brown", 45, "Female", "Fracture", PatientCategory.OUTPATIENT);
        manager.registerPatient(p);
        
        Patient found = manager.searchPatient("P102");
        assertNotNull(found);
        assertEquals("Alice", found.getFirstName());
    }

    @Test
    public void testUpdatePatientDetails() {
        Patient p = new Patient("P103", "Bob", "Green", 50, "Male", "Asthma", PatientCategory.OUTPATIENT);
        manager.registerPatient(p);

        boolean updated = manager.updatePatient("P103", "Robert", "Green", 51, "Severe Asthma");
        assertTrue(updated);
        assertEquals("Robert", manager.searchPatient("P103").getFirstName());
    }

    @Test
    public void testDeletePatient() {
        Patient p = new Patient("P104", "Charlie", "White", 20, "Male", "Checkup", PatientCategory.OUTPATIENT);
        manager.registerPatient(p);

        assertTrue(manager.deletePatient("P104"));
        assertNull(manager.searchPatient("P104"));
    }

    @Test
    public void testAllocateAndReleaseBed() {
        Inpatient inp = new Inpatient("P105", "David", "Black", 60, "Male", "Surgery", null, null);
        manager.registerPatient(inp);

        assertTrue(manager.allocateBed("P105", "B01"));
        assertTrue(manager.getOccupiedBeds().contains("B01"));

        assertTrue(manager.releaseBed("B01"));
        assertFalse(manager.getOccupiedBeds().contains("B01"));
    }

    @Test
    public void testPreventAllocatingOccupiedBed() {
        Inpatient inp1 = new Inpatient("P106", "Eva", "Grey", 35, "Female", "Observation", null, null);
        Inpatient inp2 = new Inpatient("P107", "Frank", "Blue", 40, "Male", "Recovery", null, null);
        
        manager.registerPatient(inp1);
        manager.registerPatient(inp2);

        manager.allocateBed("P106", "B02");
        assertFalse(manager.allocateBed("P107", "B02"));
    }

    @Test
    public void testPreventBedAllocationWhenFull() {
        for (int i = 1; i <= 20; i++) {
            String pid = "P" + i;
            Inpatient inp = new Inpatient(pid, "Name" + i, "Last" + i, 30, "Male", "Condition", null, null);
            manager.registerPatient(inp);
            
            String bedId = String.format("B%02d", i);
            manager.allocateBed(pid, bedId);
        }

        assertTrue(manager.getAvailableBeds().isEmpty());

        Inpatient extraInpatient = new Inpatient("P21", "Extra", "Patient", 25, "Female", "Rest", null, null);
        manager.registerPatient(extraInpatient);
        assertFalse(manager.allocateBed("P21", "B01"));
    }

    @Test
    public void testSortPatients() {
        Patient p1 = new Patient("P102", "Zoe", "Zebra", 30, "Female", "Cold", PatientCategory.OUTPATIENT);
        Patient p2 = new Patient("P101", "Adam", "Apple", 25, "Male", "Flu", PatientCategory.OUTPATIENT);

        manager.registerPatient(p1);
        manager.registerPatient(p2);

        manager.sortPatientsBySurname();
        assertEquals("Apple", manager.getAllPatients().get(0).getLastName());

        manager.sortPatientsById();
        assertEquals("P101", manager.getAllPatients().get(0).getPatientId());
    }
}