package org.example.medisched.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "Doctors")
public class Doctor {
    @Id
    @Column(name = "DoctorID", columnDefinition = "BIGINT UNSIGNED")
    private Long doctorId;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JsonIgnoreProperties({"doctor", "hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "DoctorID", insertable = false, updatable = false)
    private User user;



    @Column(name = "Specialty", nullable = false)
    private String specialty;

    @Column(name = "Qualification")
    private String qualification;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "Bio", columnDefinition = "TEXT")
    private String bio;

    // Constructors
    public Doctor() {}

    public Doctor(User user, String specialty, String qualification, Integer experienceYears, String bio) {
        this.user = user;
        this.specialty = specialty;
        this.qualification = qualification;
        this.experienceYears = experienceYears;
        this.bio = bio;
    }

    // Getters and Setters
    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
