package com.eval.InternalMedicine.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    private String roomNumber;
    private int floor;
    @OneToOne
    private Doctor doctor;
    @OneToMany(mappedBy = "room")
    private List<Appointment>appointmentList;

}
