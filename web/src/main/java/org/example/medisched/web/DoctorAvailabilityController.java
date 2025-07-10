package org.example.medisched.web;

import org.example.medisched.entity.DoctorAvailability;
import org.example.medisched.service.DoctorAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/availabilities")
public class DoctorAvailabilityController {

    @Autowired
    private DoctorAvailabilityService service;

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorAvailability>> getAvailabilitiesByDoctorId(@PathVariable Long doctorId) {
        return ResponseEntity.ok(service.getAvailabilitiesByDoctorId(doctorId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<DoctorAvailability> getAvailabilityById(@PathVariable Long id) {
        return service.getAvailabilityById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DoctorAvailability> createAvailability(@RequestBody DoctorAvailability availability) {
        return ResponseEntity.ok(service.createAvailability(availability));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorAvailability> updateAvailability(@PathVariable Long id, @RequestBody DoctorAvailability availability) {
        DoctorAvailability updated = service.updateAvailability(id, availability);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvailability(@PathVariable Long id) {
        service.deleteAvailability(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<DoctorAvailability>> getAllAvailabilities() {
        return ResponseEntity.ok(service.getAllAvailabilities());
    }


}
