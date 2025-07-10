package org.example.medisched.web;

import org.example.medisched.entity.Appointments;
import org.example.medisched.entity.Doctor;
import org.example.medisched.entity.Patients;
import org.example.medisched.dto.AppointmentRequestDTO;
import org.example.medisched.repository.DoctorRepository;
import org.example.medisched.repository.PatientsRepository;
import org.example.medisched.service.AppointmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.example.medisched.dto.AppointmentStatusUpdateDTO;
import org.example.medisched.dto.AppointmentResponseDTO;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/appointments")
public class AppointmentsController {

    @Autowired
    private AppointmentsService appointmentsService;

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientsRepository patientsRepository;


    @GetMapping
    public List<Appointments> getAll() {
        return appointmentsService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointments> getById(@PathVariable Long id) {
        return appointmentsService.getAppointmentsById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/book/{availabilityId}")
    public ResponseEntity<?> bookAppointment(@PathVariable Long availabilityId,
                                             @RequestBody AppointmentRequestDTO requestDto) {
        try {
            Doctor doctor = doctorRepository.findById(requestDto.getDoctorid())
                    .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + requestDto.getDoctorid()));
            Patients patient = patientsRepository.findById(requestDto.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + requestDto.getPatientId()));

            Appointments newAppointment = new Appointments();
            newAppointment.setDoctor(doctor);
            newAppointment.setPatient(patient);
            newAppointment.setAppointmentDate(requestDto.getAppointmentDate());
            newAppointment.setStatus(Appointments.Status.valueOf(requestDto.getStatus()));

            Appointments booked = appointmentsService.createAppointmentsFromSlot(availabilityId, newAppointment);

            return ResponseEntity.ok(booked);
        } catch (RuntimeException e) {
            System.err.println("Error booking appointment: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        appointmentsService.deleteAppointments(id);
        return ResponseEntity.noContent().build();
    }

    // --- ADD THIS NEW PUT MAPPING METHOD ---
    @PutMapping("/{id}") // Maps to PUT /api/appointments/{id}
    public ResponseEntity<Void> updateAppointmentStatus(@PathVariable Long id, @RequestBody AppointmentStatusUpdateDTO statusUpdateDto) {
        try {
            // Call service method to update status and release slot
            appointmentsService.updateAppointmentStatusAndReleaseSlot(id, statusUpdateDto.getStatus());
            return ResponseEntity.ok().build(); // 200 OK
        } catch (RuntimeException e) { // Catch specific exceptions if needed, e.g., AppointmentNotFoundException
            System.err.println("Error updating appointment status for ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Or ResponseEntity.notFound().build()
        }
    }
    @GetMapping("/patient/{patientId}") // Maps to GET /api/appointments/patient/{patientId}
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsForPatient(@PathVariable Long patientId) {
        List<AppointmentResponseDTO> appointments = appointmentsService.getAppointmentsByPatientId(patientId);
        if (appointments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(appointments);
    }

}