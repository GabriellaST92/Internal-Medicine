package com.eval.InternalMedicine.repository;

import com.eval.InternalMedicine.model.Appointment;
import com.eval.InternalMedicine.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findByDoctor_IdAndDate(Long doctorId, LocalDateTime date);
    Optional<Appointment> findByRoom_IdAndDate(Long roomId, LocalDateTime date);
    Optional<Appointment> findByPatient_IdAndDate(Long patientId, LocalDateTime date);
    @Query("SELECT a FROM Appointment a WHERE a.date >= :start AND a.date <= :end AND a.doctor = :doctor")
    List<Appointment> findAppointmentsByDoctorAndDay(@Param("start") LocalDateTime start,
                                                     @Param("end") LocalDateTime end,
                                                     @Param("doctor") Doctor doctor);
}

