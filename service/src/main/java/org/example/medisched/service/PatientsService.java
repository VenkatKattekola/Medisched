package org.example.medisched.service;

import org.example.medisched.entity.Patients;
import org.example.medisched.repository.PatientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface PatientsService {
    List<Patients> getAllPatients();
    Optional<Patients> getPatientById(Long id);
    Patients createPatient(Patients patient);
    Patients updatePatient(Long id, Patients patient);
    void deletePatient(Long id);

    @Service
    class PatientsServiceImpl implements PatientsService {

        @Autowired
        private PatientsRepository patientRepository;

        @Override
        public List<Patients> getAllPatients() {
            return patientRepository.findAll();
        }

        @Override
        public Optional<Patients> getPatientById(Long id) {
            return patientRepository.findById(id);
        }

        @Override
        public Patients createPatient(Patients patient) {
            return patientRepository.save(patient);
        }

        @Override
        public Patients updatePatient(Long id, Patients updatedPatient) {
            return patientRepository.findById(id)
                    .map(patient -> {
                        patient.setDateOfBirth(updatedPatient.getDateOfBirth());
                        patient.setGender(updatedPatient.getGender());
                        patient.setPhoneNumber(updatedPatient.getPhoneNumber());
                        patient.setAddress(updatedPatient.getAddress());
                        // If you want to update user, handle separately
                        return patientRepository.save(patient);
                    })
                    .orElse(null);
        }

        @Override
        public void deletePatient(Long id) {
            patientRepository.deleteById(id);
        }
    }
}
