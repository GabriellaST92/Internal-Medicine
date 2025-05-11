package com.eval.InternalMedicine.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;
    @ManyToOne
    private Room room;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    LocalDateTime date;
    @ManyToOne
    private Patient patient;
    private String status;

    /*public Appointment(Long roomId, Long doctorId, LocalDateTime date, Long patientId, String status) {

        this.room = room;
        this.doctor = doctor;
        this.date = date;
        this.patient = patient;
        this.status = status;
    }


    * */

}
