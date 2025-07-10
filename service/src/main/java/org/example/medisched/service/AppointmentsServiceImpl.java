// src/main/java/org/example/medisched/service/AppointmentsServiceImpl.java

package org.example.medisched.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.medisched.entity.Appointments;
import org.example.medisched.entity.DoctorAvailability;
import org.example.medisched.repository.AppointmentsRepository;
import org.example.medisched.repository.DoctorAvailabilityRepository;
import org.example.medisched.repository.DoctorRepository;
import org.example.medisched.repository.PatientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.medisched.dto.AppointmentResponseDTO;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentsServiceImpl implements AppointmentsService {

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @Autowired
    private DoctorAvailabilityRepository doctorAvailabilityRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientsRepository patientsRepository;

    @Override
    public List<Appointments> getAllAppointments() {
        return appointmentsRepository.findAll();
    }

    @Override
    public Optional<Appointments> getAppointmentsById(Long id) {
        return appointmentsRepository.findById(id);
    }
    @Override
    @Transactional // Ensure this is transactional for booking operations
    public Appointments createAppointmentsFromSlot(Long availabilityId, Appointments appointment) {
        DoctorAvailability slot = doctorAvailabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new RuntimeException("Doctor Availability slot not found with ID: " + availabilityId));

        if (slot.isBooked()) {
            throw new RuntimeException("This slot is already booked.");
        }

        slot.setIsBooked(true);
        doctorAvailabilityRepository.save(slot);

        // Set the appointment date from the slot
        appointment.setAppointmentDate(slot.getAvailableDate().atTime(slot.getStartTime()));

        // --- CRITICAL ADDITION: Link the Appointment to the booked DoctorAvailability slot ---
        appointment.setBookedAvailabilitySlot(slot);
        // --- END CRITICAL ADDITION ---

        return appointmentsRepository.save(appointment);
    }

    @Override
    public void deleteAppointments(Long id) {
        // IMPORTANT: If you delete an appointment, you should also consider releasing its
        // bookedAvailabilitySlot if it exists. Otherwise, that slot might remain 'booked'
        // even if the appointment is gone, leading to orphaned unavailable slots.
        // For simplicity now, keeping as is, but consider adding similar logic to cancellation.
        appointmentsRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponseDTO> getAppointmentsByPatientId(Long patientId) {
        List<Appointments> appointments = appointmentsRepository.findByPatient_PatientId(patientId);

        return appointments.stream()
                .map(appointment -> {
                    return new AppointmentResponseDTO(
                            appointment.getAppointmentId(),
                            appointment.getAppointmentDate(),
                            appointment.getStatus().name(),
                            appointment.getPatient().getPatientId(),
                            appointment.getDoctor().getDoctorId(),
                            appointment.getDoctor().getUser().getFullName(),
                            appointment.getDoctor().getSpecialty()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional // This operation modifies data, so it needs to be transactional
    public void updateAppointmentStatusAndReleaseSlot(Long appointmentId, String newStatus) {
        Appointments appointmentToUpdate = appointmentsRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + appointmentId));

        // Get the associated availability slot BEFORE changing the appointment's link
        DoctorAvailability bookedSlot = appointmentToUpdate.getBookedAvailabilitySlot();

        // 1. Update the appointment status
        // Assuming 'Appointments.Status' is an Enum
        appointmentToUpdate.setStatus(Appointments.Status.valueOf(newStatus));

        // 2. *** CRITICAL CHANGE: Detach the appointment from its availability slot ***
        //    This sets the 'availability_slot_id' to NULL for this appointment record in the database.
        appointmentToUpdate.setBookedAvailabilitySlot(null);

        // 3. Save the updated appointment (which now no longer references the slot)
        appointmentsRepository.save(appointmentToUpdate);

        // 4. Update the DoctorAvailability slot status to available, IF it was linked
        //    and if the new status implies releasing the slot (e.g., "CANCELLED").
        if (bookedSlot != null && newStatus.equalsIgnoreCase("CANCELLED")) {
            bookedSlot.setIsBooked(false); // Mark the slot as available again
            doctorAvailabilityRepository.save(bookedSlot); // Save the updated slot
        }
        // You can add other conditions here for statuses like "NO_SHOW" if they should also release the slot.
    }

}