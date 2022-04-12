package org.kodigo.agenda.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.kodigo.agenda.entities.Contact;
import org.kodigo.agenda.entities.Label;
import org.kodigo.agenda.repositories.ContactRepository;
import org.kodigo.agenda.repositories.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ContactsController {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ObjectMapper mapper;

    @PostMapping("/contacts/")
    public ObjectNode createContact(@Validated @RequestBody Contact contact) {

        ObjectNode data = mapper.createObjectNode();
        ObjectNode response = mapper.createObjectNode();

        Optional<Label> label = labelRepository.findById(contact.getLabel());

        if(!label.isPresent()) {
            response.put("result" , "error");
            response.put("message", "Invalid label provided");
            return response;
        }

        try {
            Contact instance = contactRepository.save(contact);
            data.put("id", instance.getId());

            response.put("result" , "ok");
            response.set("data", data);

        } catch (Exception e) {
            response.put("result" , "error");
            response.put("message", "Unable to save new instance with the data provided");
        }

        return response;
    }


    @GetMapping("/contacts/")
    public ObjectNode readContacts() {
        List<Contact> contacts  = contactRepository.findAll();
        ObjectNode response = mapper.createObjectNode();

        response.put("result" , "ok");
        response.set("data", mapper.convertValue(contacts, JsonNode.class));

        return response;
    }

    @GetMapping("/contacts/{id}")
    public ObjectNode readContact(@PathVariable("id") Integer id) {

        ObjectNode response = mapper.createObjectNode();
        Optional<Contact> contact = contactRepository.findById(id);

        if (contact.isPresent()) {
            response.put("result" , "ok");
            response.set("data", mapper.convertValue(contact.get(), JsonNode.class));
        } else {
            response.put("result" , "error");
            response.put("message", "Unable to find instance with the ID provided");
        }

        return response;
    }

    @PutMapping("/contacts/{id}")
    public ObjectNode updateContact(@PathVariable("id") Integer id, @Validated @RequestBody Contact contact) {

        ObjectNode response = mapper.createObjectNode();
        Optional<Contact> results = contactRepository.findById(id);
        Optional<Label> label = labelRepository.findById(contact.getLabel());

        if(!label.isPresent()) {
            response.put("result" , "error");
            response.put("message", "Invalid label provided");
            return response;
        }

        if(results.isPresent()) {
            contact.setId(id);
            contactRepository.save(contact);

            response.put("result" , "ok");
            response.put("message", "Successfully updated instance");
        }
        else {
            response.put("result" , "error");
            response.put("message", "Unable to find requested instance");
        }

        return response;
    }

    @DeleteMapping("/contacts/{id}")
    public ObjectNode deleteContact(@PathVariable("id") Integer id) {

        ObjectNode response = mapper.createObjectNode();
        Optional<Contact> contact = contactRepository.findById(id);

        if(contact.isPresent()) {
            contactRepository.deleteById(id);

            response.put("result" , "ok");
            response.put("message", "Successfully deleted instance");
        } else {
            response.put("result" , "error");
            response.put("message", "Unable to find requested instance");
        }

        return response;
    }
}
