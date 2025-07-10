package org.example.medisched.web;

import org.example.medisched.entity.Patients;
import org.example.medisched.service.AppointmentsService;
import org.example.medisched.service.PatientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.medisched.dto.AppointmentResponseDTO; // Import this DTO

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/patients")
public class PatientsController {

    @Autowired
    private PatientsService patientsService;

    @Autowired
    private AppointmentsService appointmentsService; // Inject AppointmentsService

    @GetMapping
    public ResponseEntity<List<Patients>> getAllPatients() {
        List<Patients> patients = patientsService.getAllPatients();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patients> getPatientById(@PathVariable Long id) {
        Optional<Patients> patient = patientsService.getPatientById(id);
        return patient.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Patients> createPatient(@RequestBody Patients patient) {
        Patients createdPatient = patientsService.createPatient(patient);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patients> updatePatient(@PathVariable Long id, @RequestBody Patients updatedPatient) {
        Patients patient = patientsService.updatePatient(id, updatedPatient);
        return patient != null ? new ResponseEntity<>(patient, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePatient(@PathVariable Long id) {
        patientsService.deletePatient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // --- ADD THIS METHOD ---
    /**
     * Retrieves all appointments for a specific patient.
     * Endpoint: GET /api/patients/{patientId}/appointments
     * @param patientId The ID of the patient.
     * @return A list of AppointmentResponseDTOs for the given patient.
     */
    @GetMapping("/{patientId}/appointments") // This maps the specific path
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsForPatient(@PathVariable Long patientId) {
        try {
            List<AppointmentResponseDTO> appointments = appointmentsService.getAppointmentsByPatientId(patientId);
            if (appointments.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(appointments); // Return 200 OK with empty list if no appointments
            }
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            System.err.println("Error fetching appointments for patient " + patientId + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // --- END ADDED METHOD ---
}