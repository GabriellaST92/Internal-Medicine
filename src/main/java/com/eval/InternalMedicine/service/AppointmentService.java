package com.eval.InternalMedicine.service;

import com.eval.InternalMedicine.exception.InvalidAppointmentException;
import com.eval.InternalMedicine.exception.TimeConflictException;
import com.eval.InternalMedicine.model.Appointment;
import com.eval.InternalMedicine.model.Doctor;
import com.eval.InternalMedicine.model.Patient;
import com.eval.InternalMedicine.model.Room;
import com.eval.InternalMedicine.repository.AppointmentRepository;
import com.eval.InternalMedicine.repository.DoctorRepository;
import com.eval.InternalMedicine.repository.PatientRepository;
import com.eval.InternalMedicine.repository.RoomRepository;
import org.hibernate.grammars.hql.HqlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private PatientRepository patientRepository;

    public List<Appointment>getAppointmentsByDoctorAndDay(Doctor doctor, LocalDateTime date){
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfName = date.toLocalDate().atTime(LocalTime.MAX);
        return appointmentRepository.findAppointmentsByDoctorAndDay(startOfDay, endOfName, doctor);
    }

    public Optional<Appointment>getAllAppointmentsByDoctorIdAndDate(
            @RequestParam Long id,
            @RequestParam LocalDateTime date){
        return appointmentRepository.findByDoctorIdAndDate(id, date);
    }

    public Optional<Appointment> getAllAppointmentsByRoomAndDate(
            @RequestParam Long roomId,
            @RequestParam LocalDateTime date
    ){
        return appointmentRepository.findByRoomAndDate(roomId, date);
    }

    public Optional<Appointment> findAppointmentsByPatientAndDate(
            @RequestParam Long patientId,
            @RequestParam LocalDateTime date
    ){
        return appointmentRepository.findByPatientAndDate(patientId, date);
    }

    public boolean deleteIfPending(Long appointmentId){
        Optional<Appointment> optional = appointmentRepository.findById(appointmentId);
        if(optional.isPresent()){
            Appointment appointment = optional.get();
            if("Pending".equalsIgnoreCase(appointment.getStatus())){
                appointmentRepository.deleteById(appointmentId);
                return true;
            }
        }
        return false;
    }

    public Appointment createAppointment(Appointment appt){
        
        Optional<Appointment> optional = this.getAllAppointmentsByRoomAndDate(appt.getRoom().getRoomId(), appt.getDate());
        //No en el mismo consultorio ni a la misma hora
        if(optional.isPresent()){
            throw  new TimeConflictException("The requested room is not available at the selected time");
        }
        //No para el mismo doctor a la misma hora
        optional = this.getAllAppointmentsByDoctorIdAndDate(appt.getDoctor().getDoctorId(), appt.getDate());
        if(optional.isPresent()){
            throw  new TimeConflictException("The doctor requested is not available at the selected time");
        }
        //No al mismo paciente a la misma hora ni con menos de 2 horas de diferencia el mismo día
        optional= this.findAppointmentsByPatientAndDate(appt.getPatient().getId(), appt.getDate());

        if(optional.isPresent()){
            throw  new InvalidAppointmentException("The patient have an appointment in the selected time, please select a different time");
        }
        else{
            Duration difference = Duration.between(optional.get().getDate(), appt.getDate());
            if( difference.toHours() < 2 ){
                throw  new InvalidAppointmentException("You cannot schedule this appointment. The patient already has another appointment within two hours of the selected time");
            }
        }

        //Un mismo doctor solo puede tener hasta 8 citas al día
        List<Appointment>doctorsAppointmentsOfDay = this.getAppointmentsByDoctorAndDay(appt.getDoctor(), appt.getDate());
        if(doctorsAppointmentsOfDay.size() >= 8){
            throw  new TimeConflictException("The doctor selected is not available the requested day. Please select a different day");
        }
        return appointmentRepository.save(appt);
    }

    public Appointment updateAppointment(Appointment appt){
        Optional<Appointment> optional = appointmentRepository.findById( appt.getAppointmentId());
        if(optional.isEmpty()){
            throw new RuntimeException("Appointment not found with id: "+ appt.getAppointmentId());
        }
        Appointment appointment = optional.get();

        appointment.setDate(appt.getDate());
        appointment.setRoom(appt.getRoom());
        if(appt.getDoctor() != null){
            Doctor doctor = doctorRepository.findById(appt.getDoctor().getDoctorId())
                    .orElseThrow(()-> new RuntimeException(("Doctor not found")));
            appointment.setDoctor(doctor);
        }
        if(appt.getPatient() != null){
            Patient patient = patientRepository.findById(appt.getPatient().getId())
                    .orElseThrow(()-> new RuntimeException(("Patient not found")));
            appointment.setPatient(patient);
        }
        if(appt.getRoom() != null){
            Room room = roomRepository.findById(appt.getRoom().getRoomId())
                    .orElseThrow(()-> new RuntimeException(("Room not found")));
            appointment.setRoom(room);
        }
        return createAppointment(appointment);
    }

}
