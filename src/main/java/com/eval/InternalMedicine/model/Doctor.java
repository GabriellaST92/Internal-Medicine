package com.eval.InternalMedicine.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {
    @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String paternalName;
    private String maternalName;
    private String specialty;
    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointmentList;

}
