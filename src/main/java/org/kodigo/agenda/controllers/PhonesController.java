package org.kodigo.agenda.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.kodigo.agenda.entities.Contact;
import org.kodigo.agenda.entities.Phone;
import org.kodigo.agenda.entities.Type;
import org.kodigo.agenda.repositories.ContactRepository;
import org.kodigo.agenda.repositories.PhoneRepository;
import org.kodigo.agenda.repositories.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class PhonesController {

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private ObjectMapper mapper;


    @PostMapping("/phones/")
    public ObjectNode createPhone(@Validated @RequestBody Phone newPhone) {

        ObjectNode metadata = mapper.createObjectNode();
        ObjectNode response = mapper.createObjectNode();

        Optional<Type> type = typeRepository.findById(newPhone.getType());
        Optional<Contact> contact = contactRepository.findById(newPhone.getContact());

        if(!type.isPresent()) {
            response.put("result" , "error");
            response.put("message", "Invalid type provided");
            return response;
        }

        if(!contact.isPresent()) {
            response.put("result" , "error");
            response.put("message", "Invalid contact provided");
            return response;
        }

        if(newPhone.getNumber().isEmpty()) {
            response.put("result" , "error");
            response.put("message", "A valid phone number is required");
            return response;
        }

        try {
            Phone instance = phoneRepository.save(newPhone);
            metadata.put("id", instance.getId());
            response.put("result" , "ok");
            response.set("data", metadata);

        } catch (Exception e) {
            response.put("result" , "error");
            response.put("message", "Unable to save new instance with the data provided");
        }

        return response;
    }


    @GetMapping("/phones/")
    public ObjectNode readPhones() {
        List<Phone> phones  = phoneRepository.findAll();
        ObjectNode response = mapper.createObjectNode();

        response.put("result" , "ok");
        response.set("data", mapper.convertValue(phones, JsonNode.class));

        return response;
    }


    @GetMapping("/phones/{id}")
    public ObjectNode readPhone(@PathVariable("id") Integer id) {

        ObjectNode response   = mapper.createObjectNode();
        Optional<Phone> phone = phoneRepository.findById(id);

        if (phone.isPresent()) {
            response.put("result" , "ok");
            response.set("data", mapper.convertValue(phone.get(), JsonNode.class));
        } else {
            response.put("result" , "error");
            response.put("message", "Unable to find instance with the ID provided");
        }

        return response;
    }


    @PutMapping("/phones/{id}")
    public ObjectNode updatePhone(@PathVariable("id") Integer id, @RequestBody Phone phone) {

        ObjectNode response = mapper.createObjectNode();
        Optional<Phone> results = phoneRepository.findById(id);

        Optional<Type> type = typeRepository.findById(phone.getType());
        Optional<Contact> contact = contactRepository.findById(phone.getContact());

        if(!type.isPresent()) {
            response.put("result" , "error");
            response.put("message", "Invalid type provided");
            return response;
        }

        if(!contact.isPresent()) {
            response.put("result" , "error");
            response.put("message", "Invalid contact provided");
            return response;
        }

        if(phone.getNumber().isEmpty()) {
            response.put("result" , "error");
            response.put("message", "A valid phone number is required");
            return response;
        }

        if(results.isPresent()) {
            phone.setId(id);
            phoneRepository.save(phone);

            response.put("result" , "ok");
            response.put("message", "Successfully updated instance");
        }
        else {
            response.put("result" , "error");
            response.put("message", "Unable to find requested instance");
        }

        return response;
    }


    @DeleteMapping("/phones/{id}")
    public ObjectNode deletePhone(@PathVariable("id") Integer id) {

        ObjectNode response = mapper.createObjectNode();
        Optional<Phone> phone = phoneRepository.findById(id);

        if(phone.isPresent()) {
            phoneRepository.deleteById(id);

            response.put("result" , "ok");
            response.put("message", "Successfully deleted instance");
        } else {
            response.put("result" , "error");
            response.put("message", "Unable to find requested instance");
        }

        return response;
    }
}
