package org.kodigo.agenda.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.kodigo.agenda.entities.Contact;
import org.kodigo.agenda.entities.Email;
import org.kodigo.agenda.entities.Type;
import org.kodigo.agenda.repositories.ContactRepository;
import org.kodigo.agenda.repositories.EmailRepository;
import org.kodigo.agenda.repositories.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class EmailsController {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private ObjectMapper mapper;


    @PostMapping("/emails/")
    public ObjectNode createEmail(@Validated @RequestBody Email newEmail) {

        ObjectNode metadata = mapper.createObjectNode();
        ObjectNode response = mapper.createObjectNode();

        Optional<Type> type = typeRepository.findById(newEmail.getType());
        Optional<Contact> contact = contactRepository.findById(newEmail.getContact());

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

        if(newEmail.getAddress().isEmpty()) {
            response.put("result" , "error");
            response.put("message", "A valid email address is required");
            return response;
        }

        try {
            Email instance = emailRepository.save(newEmail);
            metadata.put("id", instance.getId());
            response.put("result" , "ok");
            response.set("data", metadata);

        } catch (Exception e) {
            response.put("result" , "error");
            response.put("message", "Unable to save new instance with the data provided");
        }

        return response;
    }


    @GetMapping("/emails/")
    public ObjectNode readEmails() {
        List<Email> emails  = emailRepository.findAll();
        ObjectNode response = mapper.createObjectNode();

        response.put("result" , "ok");
        response.set("data", mapper.convertValue(emails, JsonNode.class));

        return response;
    }


    @GetMapping("/emails/{id}")
    public ObjectNode readEmail(@PathVariable("id") Integer id) {

        ObjectNode response   = mapper.createObjectNode();
        Optional<Email> email = emailRepository.findById(id);

        if (email.isPresent()) {
            response.put("result" , "ok");
            response.set("data", mapper.convertValue(email.get(), JsonNode.class));
        } else {
            response.put("result" , "error");
            response.put("message", "Unable to find instance with the ID provided");
        }

        return response;
    }


    @PutMapping("/emails/{id}")
    public ObjectNode updateEmail(@PathVariable("id") Integer id, @RequestBody Email email) {

        ObjectNode response = mapper.createObjectNode();
        Optional<Email> results = emailRepository.findById(id);

        Optional<Type> type = typeRepository.findById(email.getType());
        Optional<Contact> contact = contactRepository.findById(email.getContact());

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

        if(email.getAddress().isEmpty()) {
            response.put("result" , "error");
            response.put("message", "A valid email number is required");
            return response;
        }

        if(results.isPresent()) {
            email.setId(id);
            emailRepository.save(email);

            response.put("result" , "ok");
            response.put("message", "Successfully updated instance");
        }
        else {
            response.put("result" , "error");
            response.put("message", "Unable to find requested instance");
        }

        return response;
    }


    @DeleteMapping("/emails/{id}")
    public ObjectNode deleteEmail(@PathVariable("id") Integer id) {

        ObjectNode response = mapper.createObjectNode();
        Optional<Email> email = emailRepository.findById(id);

        if(email.isPresent()) {
            emailRepository.deleteById(id);

            response.put("result" , "ok");
            response.put("message", "Successfully deleted instance");
        } else {
            response.put("result" , "error");
            response.put("message", "Unable to find requested instance");
        }

        return response;
    }
}
