package com.eval.InternalMedicine.service;

import com.eval.InternalMedicine.model.Patient;
import com.eval.InternalMedicine.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> getAllPatients(){
        return patientRepository.findAll();
    }
}
