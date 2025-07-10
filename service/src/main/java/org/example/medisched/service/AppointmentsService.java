package org.example.medisched.service;

import org.example.medisched.entity.Appointments;
import org.example.medisched.dto.AppointmentResponseDTO;

import java.util.List;
import java.util.Optional;

// This is your INTERFACE
public interface AppointmentsService {
    List<Appointments> getAllAppointments();
    Optional<Appointments> getAppointmentsById(Long id);
    Appointments createAppointmentsFromSlot(Long availabilityId, Appointments appointment);
    void deleteAppointments(Long id);
    void updateAppointmentStatusAndReleaseSlot(Long appointmentId, String newStatus); // Your new method
    List<AppointmentResponseDTO> getAppointmentsByPatientId(Long patientId); // The method used by PatientsController
}