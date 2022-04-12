package org.kodigo.agenda.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.kodigo.agenda.entities.Appointment;
import org.kodigo.agenda.entities.Contact;
import org.kodigo.agenda.entities.Label;
import org.kodigo.agenda.repositories.AppointmentRepository;
import org.kodigo.agenda.repositories.ContactRepository;
import org.kodigo.agenda.repositories.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AppointmentsController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ObjectMapper mapper;

    @PostMapping("/appointments/")
    public ObjectNode createAppointment(@Validated @RequestBody Appointment appointment) {

        ObjectNode data = mapper.createObjectNode();
        ObjectNode response = mapper.createObjectNode();

        Optional<Contact> contact = contactRepository.findById(appointment.getContact());

        if(!contact.isPresent()) {
            response.put("result" , "error");
            response.put("message", "Invalid label provided");
            return response;
        }

        List<Appointment> conflictingAppointments = appointmentRepository.findConflictingAppointments(appointment.getStarts(), appointment.getEnds());

        if(conflictingAppointments.size()>0) {
            response.put("result" , "error");
            response.put("message", "There is another appointment overlapping those hours");
            return response;
        }

        try {
            Appointment instance = appointmentRepository.save(appointment);
            data.put("id", instance.getId());

            response.put("result" , "ok");
            response.set("data", data);

        } catch (Exception e) {
            response.put("result" , "error");
            response.put("message", "Unable to save new instance with the data provided");
        }

        return response;

    }


    @GetMapping("/appointments/")
    public ObjectNode readAppointments() {
        List<Appointment> appointments  = appointmentRepository.findAll();
        ObjectNode response = mapper.createObjectNode();

        response.put("result" , "ok");
        response.set("data", mapper.convertValue(appointments, JsonNode.class));

        return response;
    }

    @GetMapping("/appointments/{id}")
    public ObjectNode readAppointment(@PathVariable("id") Integer id) {

        ObjectNode response = mapper.createObjectNode();
        Optional<Appointment> appointment = appointmentRepository.findById(id);

        if (appointment.isPresent()) {
            response.put("result" , "ok");
            response.set("data", mapper.convertValue(appointment.get(), JsonNode.class));
        } else {
            response.put("result" , "error");
            response.put("message", "Unable to find instance with the ID provided");
        }

        return response;
    }

    @PutMapping("/appointments/{id}")
    public ObjectNode updateAppointment(@PathVariable("id") Integer id, @Validated @RequestBody Appointment appointment) {

        ObjectNode response = mapper.createObjectNode();
        Optional<Appointment> results = appointmentRepository.findById(id);
        Optional<Contact> contact = contactRepository.findById(appointment.getContact());

        if(!contact.isPresent()) {
            response.put("result" , "error");
            response.put("message", "Invalid label provided");
            return response;
        }

        if(results.isPresent()) {
            appointment.setId(id);
            appointmentRepository.save(appointment);

            response.put("result" , "ok");
            response.put("message", "Successfully updated instance");
        }
        else {
            response.put("result" , "error");
            response.put("message", "Unable to find requested instance");
        }

        return response;
    }

    @DeleteMapping("/appointments/{id}")
    public ObjectNode deleteAppointment(@PathVariable("id") Integer id) {

        ObjectNode response = mapper.createObjectNode();
        Optional<Appointment> appointment = appointmentRepository.findById(id);

        if(appointment.isPresent()) {
            appointmentRepository.deleteById(id);

            response.put("result" , "ok");
            response.put("message", "Successfully deleted instance");
        } else {
            response.put("result" , "error");
            response.put("message", "Unable to find requested instance");
        }

        return response;
    }
}
