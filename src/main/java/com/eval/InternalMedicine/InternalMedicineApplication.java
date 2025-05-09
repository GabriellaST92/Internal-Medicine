package com.eval.InternalMedicine;

import com.eval.InternalMedicine.model.Doctor;
import com.eval.InternalMedicine.model.Patient;
import com.eval.InternalMedicine.repository.DoctorRepository;
import com.eval.InternalMedicine.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class InternalMedicineApplication {

	public static void main(String[] args) {
		SpringApplication.run(InternalMedicineApplication.class, args);
	}

	@Bean
	CommandLineRunner initData (DoctorRepository docRepo, PatientRepository patientRepo){
		return args -> {
			Doctor doc1 = new Doctor(null, "Juan", "Perez", "Perez", "Cardiology", new ArrayList<>());
			Doctor doc2 = new Doctor(null, "Juana", "Lopez", "Lopez", "Dermatology", new ArrayList<>());
			Doctor doc3 = new Doctor(null, "Laura", "Garcia", "Garcia", "Pediatrics", new ArrayList<>());
			docRepo.saveAll(List.of(doc1, doc2, doc3));

			Patient patient1 = new Patient();
			patient1.setName("Sara");
			patient1.setMaternalName("Salas");
			Patient patient2 = new Patient();
			patient2.setName("Andres");
			patient2.setMaternalName("Doe");
			patient2.setPaternalName("Doe");
			Patient patient3 = new Patient();
			patient3.setName("Jose");
			patient3.setMaternalName("Campos");
			patient3.setPaternalName("Campos");
			patientRepo.saveAll(List.of(patient1, patient2, patient3));
		};
	}

}
