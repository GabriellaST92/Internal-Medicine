package com.eval.InternalMedicine.controller;

import com.eval.InternalMedicine.model.Doctor;
import com.eval.InternalMedicine.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping
    public List<Doctor>getAllDoctors(){
        return doctorRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Doctor> getDoctorById(@PathVariable Long id){

        return doctorRepository.findById(id);
    }

    @PostMapping("/save")
    public Doctor saveDoctor(@RequestBody Doctor doctor){

        return doctorRepository.save(doctor);
    }

    @PutMapping("/{id}")
    public Doctor updateDoctor(
            @PathVariable Long id,
            @RequestBody Doctor doctorDetails){

        return doctorRepository.findById(doctorDetails.getId()).map(doctor -> {
            doctor.setName(doctorDetails.getName());
            doctor.setPaternalName((doctorDetails.getPaternalName()));
            doctor.setMaternalName(doctorDetails.getMaternalName());
            doctor.setSpecialty(doctorDetails.getSpecialty());
            return doctorRepository.save(doctor);
        }).orElseThrow(()->new RuntimeException("Doctor not found with id: "+id));
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id){

        doctorRepository.deleteById(id);
    }
}
