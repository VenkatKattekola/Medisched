package org.example.medisched.repository;

import org.example.medisched.entity.Appointments;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentsRepository extends JpaRepository<Appointments, Long> {
    List<Appointments> findByPatient_PatientId(Long patientId);
    // In AppointmentsRepository.java
    List<Appointments> findByPatient_PatientIdAndStatus(Long patientId, Appointments.Status status);
}
