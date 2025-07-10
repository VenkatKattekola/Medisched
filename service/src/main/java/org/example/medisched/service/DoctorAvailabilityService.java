package org.example.medisched.service;

import org.example.medisched.entity.DoctorAvailability;
import org.example.medisched.repository.DoctorAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DoctorAvailabilityService {
    List<DoctorAvailability> getAllAvailabilities();
    Optional<DoctorAvailability> getAvailabilityById(Long id);
    DoctorAvailability createAvailability(DoctorAvailability availability);
    DoctorAvailability updateAvailability(Long id, DoctorAvailability availability);
    void deleteAvailability(Long id);
    List<DoctorAvailability> getAvailabilitiesByDoctorId(Long doctorId);

    @Service
    class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {

        @Autowired
        private DoctorAvailabilityRepository repository;

        @Override
        public List<DoctorAvailability> getAllAvailabilities() {
            return repository.findAll();
        }
        public List<DoctorAvailability> getAvailabilitiesByDoctorId(Long doctorId) {
            return repository.findByDoctor_DoctorId(doctorId);
        }


        @Override
        public Optional<DoctorAvailability> getAvailabilityById(Long id) {
            return repository.findById(id);
        }

        @Override
        public DoctorAvailability createAvailability(DoctorAvailability availability) {
            return repository.save(availability);
        }

        @Override
        public DoctorAvailability updateAvailability(Long id, DoctorAvailability availability) {
            return repository.findById(id).map(existing -> {
                existing.setDoctor(availability.getDoctor());
                existing.setAvailableDate(availability.getAvailableDate());
                existing.setStartTime(availability.getStartTime());
                existing.setEndTime(availability.getEndTime());
                existing.setIsBooked(availability.isBooked());
                return repository.save(existing);
            }).orElse(null);
        }

        @Override
        public void deleteAvailability(Long id) {
            repository.deleteById(id);
        }
    }
}
