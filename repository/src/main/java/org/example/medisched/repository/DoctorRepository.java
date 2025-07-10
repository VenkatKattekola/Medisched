package org.example.medisched.repository;

import org.example.medisched.entity.Doctor;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Query("SELECT d FROM Doctor d JOIN FETCH d.user u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Doctor> findByUserFullNameContainingIgnoreCase(@Param("name") String name);
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);
}
