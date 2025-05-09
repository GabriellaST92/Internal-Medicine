package com.eval.InternalMedicine.service;

import com.eval.InternalMedicine.model.Appointment;
import com.eval.InternalMedicine.model.Doctor;
import com.eval.InternalMedicine.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll();
    }


}
