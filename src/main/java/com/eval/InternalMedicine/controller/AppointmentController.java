package com.eval.InternalMedicine.controller;

import com.eval.InternalMedicine.model.Appointment;
import com.eval.InternalMedicine.model.Patient;
import com.eval.InternalMedicine.service.AppointmentService;
import com.eval.InternalMedicine.service.DoctorService;
import com.eval.InternalMedicine.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService apptService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
    @GetMapping
    public String test() {
        return "API is working!";
    }

    /*@PostMapping("/save")
    public Appointment saveAppointment(@RequestBody Appointment appt){
        return apptService.saveAppointment(appt);
    }*/

    public String showAppointmentForm(Model model){
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("patients", patientService.getAllPatients());
        return "appointment-form";
    }

    @PostMapping("/appointments")
    public String saveAppointment(@ModelAttribute Appointment appointment) {
        apptService.saveAppointment(appointment);
        return "redirect:/appointments";
    }

}
