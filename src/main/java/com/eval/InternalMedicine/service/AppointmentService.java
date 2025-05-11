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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
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
        return appointmentRepository.findByRoomIdAndDate(roomId, date);
    }

    public Optional<Appointment> findAppointmentsByPatientAndDate(
            @RequestParam Long patientId,
            @RequestParam LocalDateTime date
    ){
        return appointmentRepository.findByPatient_IdAndDate(patientId, date);
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



    public Appointment createAppointment(Long roomId, Long doctorId, LocalDateTime date, Long patientId, String status) {

        Appointment appt = new Appointment();
        Room room = roomRepository.findById(roomId).orElseThrow();
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        Patient patient = patientRepository.findById(patientId).orElseThrow();

        appt.setStatus(status);
        appt.setRoom(room);
        appt.setDate(date);
        appt.setDoctor(doctor);
        appt.setPatient(patient);

        return saveAppointment(appt);
    }

    public Appointment saveAppointment(Appointment appt){

        Optional<Appointment> optional;

        optional = this.getAllAppointmentsByRoomAndDate(appt.getRoom().getId(), appt.getDate());
        // Validar que no haya otra cita en el mismo consultorio y a la misma hora
        if(optional.isPresent()){
            throw  new TimeConflictException("The requested room is not available at the selected time");
        }
        // Validar que el doctor no tenga otra cita a la misma hora
        optional = this.getAllAppointmentsByDoctorIdAndDate(appt.getDoctor().getId(), appt.getDate());
        if(optional.isPresent()){
            throw  new TimeConflictException("The doctor requested is not available at the selected time");
        }
        // Validar que el paciente no tenga otra cita a la misma hora o dentro de 2 horas el mismo día
        optional= this.findAppointmentsByPatientAndDate(appt.getPatient().getId(), appt.getDate());

        optional.ifPresent(existingAppointment->{
            Duration difference = Duration.between(existingAppointment.getDate(), appt.getDate());
            if( difference.toHours() < 2 ){
                throw  new InvalidAppointmentException("You cannot schedule this appointment. The patient already has another appointment within two hours of the selected time");
            }
            else{
                throw  new InvalidAppointmentException("The patient have an appointment in the selected time, please select a different time");
            }

        });

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

            appointment.setDoctor(appt.getDoctor());
        }
        if(appt.getPatient() != null){
            appointment.setPatient(appt.getPatient());
        }
        if(appt.getRoom() != null){
            appointment.setRoom(appt.getRoom());
        }
        return saveAppointment(appointment);
    }

}
