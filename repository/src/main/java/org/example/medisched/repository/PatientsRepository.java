package org.example.medisched.repository;

import aj.org.objectweb.asm.commons.Remapper;
import org.example.medisched.entity.Patients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientsRepository extends JpaRepository<Patients, Long> {
    // You can keep findByUserId if you use @Query
    @Query("SELECT p FROM Patients p WHERE p.user.userId = :userId")
    Optional<Patients> findByUserId(@Param("userId") Long userId); // This would also work if you prefer this method name
}

