package org.example.medisched.service;

import org.example.medisched.entity.Doctor;
import org.example.medisched.entity.DoctorAvailability;
import org.example.medisched.entity.User;
import org.example.medisched.repository.DoctorAvailabilityRepository;
import org.example.medisched.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    List<Doctor> getAllDoctors();
    List<Doctor> searchDoctorsByName(String name);
    Optional<Doctor> getDoctorById(Long id);
    Doctor createDoctor(Doctor doctor);
    Doctor updateDoctor(Long id, Doctor doctor);
    void deleteDoctor(Long id);
    List<Doctor> getDoctorsBySpecialty(String specialty);
    List<DoctorAvailability> getAvailabilityByDoctorId(Long doctorId);
    List<DoctorAvailability> getAllAvailabilities();

}
