package com.eval.InternalMedicine.controller;

import com.eval.InternalMedicine.model.Doctor;
import com.eval.InternalMedicine.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    @Autowired
    private DoctorRepository doctorRepository;

    public List<Doctor>getAllDoctors(){
        return doctorRepository.findAll();
    }

    public Optional<Doctor> getDoctorById(Long id){
        return doctorRepository.findById(id);
    }

    public Doctor saveDoctor(Doctor doctor){
        return doctorRepository.save(doctor);
    }
    public Doctor updateDoctor(Long id, Doctor doctorDetails){
        return doctorRepository.findById(doctorDetails.getDoctorId()).map(doctor -> {
            doctor.setName(doctorDetails.getName());
            doctor.setPaternalName((doctorDetails.getPaternalName()));
            doctor.setMaternalName(doctorDetails.getMaternalName());
            doctor.setSpecialty(doctorDetails.getSpecialty());
            return doctorRepository.save(doctor);
        }).orElseThrow(()->new RuntimeException("Doctor not found with id: "+id));
    }

    public void deleteDoctor(Long id){
        doctorRepository.deleteById(id);
    }
}
