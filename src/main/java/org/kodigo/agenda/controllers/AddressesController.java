package org.kodigo.agenda.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.kodigo.agenda.entities.Contact;
import org.kodigo.agenda.entities.Address;
import org.kodigo.agenda.entities.Type;
import org.kodigo.agenda.repositories.ContactRepository;
import org.kodigo.agenda.repositories.AddressRepository;
import org.kodigo.agenda.repositories.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class AddressesController {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private ObjectMapper mapper;


    @PostMapping("/addresses/")
    public ObjectNode createAddress(@Validated @RequestBody Address newAddress) {

        ObjectNode metadata = mapper.createObjectNode();
        ObjectNode response = mapper.createObjectNode();

        Optional<Type> type = typeRepository.findById(newAddress.getType());
        Optional<Contact> contact = contactRepository.findById(newAddress.getContact());

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

        if(newAddress.getAddress().isEmpty()) {
            response.put("result" , "error");
            response.put("message", "A valid address number is required");
            return response;
        }

        try {
            Address instance = addressRepository.save(newAddress);
            metadata.put("id", instance.getId());
            response.put("result" , "ok");
            response.set("data", metadata);

        } catch (Exception e) {
            response.put("result" , "error");
            response.put("message", "Unable to save new instance with the data provided");
        }

        return response;
    }


    @GetMapping("/addresses/")
    public ObjectNode readAddresses() {
        List<Address> addresses  = addressRepository.findAll();
        ObjectNode response = mapper.createObjectNode();

        response.put("result" , "ok");
        response.set("data", mapper.convertValue(addresses, JsonNode.class));

        return response;
    }


    @GetMapping("/addresses/{id}")
    public ObjectNode readAddress(@PathVariable("id") Integer id) {

        ObjectNode response   = mapper.createObjectNode();
        Optional<Address> address = addressRepository.findById(id);

        if (address.isPresent()) {
            response.put("result" , "ok");
            response.set("data", mapper.convertValue(address.get(), JsonNode.class));
        } else {
            response.put("result" , "error");
            response.put("message", "Unable to find instance with the ID provided");
        }

        return response;
    }


    @PutMapping("/addresses/{id}")
    public ObjectNode updateAddress(@PathVariable("id") Integer id, @RequestBody Address address) {

        ObjectNode response = mapper.createObjectNode();
        Optional<Address> results = addressRepository.findById(id);

        Optional<Type> type = typeRepository.findById(address.getType());
        Optional<Contact> contact = contactRepository.findById(address.getContact());

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

        if(address.getAddress().isEmpty()) {
            response.put("result" , "error");
            response.put("message", "A valid address number is required");
            return response;
        }

        if(results.isPresent()) {
            address.setId(id);
            addressRepository.save(address);

            response.put("result" , "ok");
            response.put("message", "Successfully updated instance");
        }
        else {
            response.put("result" , "error");
            response.put("message", "Unable to find requested instance");
        }

        return response;
    }


    @DeleteMapping("/addresses/{id}")
    public ObjectNode deleteAddress(@PathVariable("id") Integer id) {

        ObjectNode response = mapper.createObjectNode();
        Optional<Address> address = addressRepository.findById(id);

        if(address.isPresent()) {
            addressRepository.deleteById(id);

            response.put("result" , "ok");
            response.put("message", "Successfully deleted instance");
        } else {
            response.put("result" , "error");
            response.put("message", "Unable to find requested instance");
        }

        return response;
    }
}
