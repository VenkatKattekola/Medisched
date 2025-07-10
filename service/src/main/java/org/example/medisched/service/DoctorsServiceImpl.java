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

@Service
public class DoctorsServiceImpl implements DoctorService {

    @Autowired
    private DoctorAvailabilityRepository doctorAvailabilityRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public List<DoctorAvailability> getAvailabilityByDoctorId(Long doctorId) {
        return doctorAvailabilityRepository.findByDoctor_DoctorId(doctorId);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    @Override
    public Doctor createDoctor(Doctor doctor) {
        doctor.getUser().setRole(User.Role.DOCTOR);
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        return doctorRepository.findById(id)
                .map(existingDoctor -> {
                    existingDoctor.setSpecialty(updatedDoctor.getSpecialty());
                    existingDoctor.setQualification(updatedDoctor.getQualification());
                    existingDoctor.setExperienceYears(updatedDoctor.getExperienceYears());
                    existingDoctor.setBio(updatedDoctor.getBio());
                    return doctorRepository.save(existingDoctor);
                })
                .orElse(null);
    }

    @Override
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public List<Doctor> searchDoctorsByName(String name) {
        return doctorRepository.findByUserFullNameContainingIgnoreCase(name);
    }

    @Override
    public List<Doctor> getDoctorsBySpecialty(String specialty) {
        return doctorRepository.findBySpecialtyIgnoreCase(specialty);
    }
    @Override
    public List<DoctorAvailability> getAllAvailabilities() {
        return doctorAvailabilityRepository.findAll();
    }

}
