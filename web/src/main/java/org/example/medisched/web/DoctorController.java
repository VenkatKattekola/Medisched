package org.example.medisched.web;

import org.example.medisched.entity.Doctor;
import org.example.medisched.entity.DoctorAvailability;
import org.example.medisched.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // COMBINED METHOD FOR GETTING ALL DOCTORS OR DOCTORS BY SPECIALTY
    @GetMapping
    public ResponseEntity<List<Doctor>> getDoctors(@RequestParam(value = "specialty", required = false) String specialty) {
        List<Doctor> doctors;
        if (specialty != null && !specialty.trim().isEmpty()) {
            doctors = doctorService.getDoctorsBySpecialty(specialty); // Use the service method for specialty
        } else {
            doctors = doctorService.getAllDoctors(); // Get all if no specialty or empty specialty
        }
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Optional<Doctor> doctor = doctorService.getDoctorById(id);
        return doctor.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        Doctor createdDoctor = doctorService.createDoctor(doctor);
        return new ResponseEntity<>(createdDoctor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor updatedDoctor) {
        Doctor doctor = doctorService.updateDoctor(id, updatedDoctor);
        return doctor != null ? new ResponseEntity<>(doctor, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/search")
    public List<Doctor> searchDoctors(@RequestParam(value = "name", required = false) String name) {
        if (name == null || name.trim().length() < 3) {
            return new ArrayList<>();
        }
        return doctorService.searchDoctorsByName(name);
    }
    @GetMapping("/{doctorId}/availability")
    public ResponseEntity<List<DoctorAvailability>> getDoctorAvailability(@PathVariable Long doctorId) {
        List<DoctorAvailability> availabilityList = doctorService.getAvailabilityByDoctorId(doctorId);
        if (availabilityList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(availabilityList, HttpStatus.OK);
    }
    // REMOVE THIS METHOD, AS ITS LOGIC IS NOW IN getDoctors()
    // @GetMapping(params = "specialty")
    // public ResponseEntity<List<Doctor>> getDoctorsBySpecialty(@RequestParam String specialty) {
    //     List<Doctor> doctors = doctorService.getDoctorsBySpecialty(specialty);
    //     return new ResponseEntity<>(doctors, HttpStatus.OK);
    // }

}