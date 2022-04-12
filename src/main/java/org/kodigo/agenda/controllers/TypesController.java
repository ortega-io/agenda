package org.kodigo.agenda.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.kodigo.agenda.entities.Type;
import org.kodigo.agenda.repositories.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TypesController {

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private ObjectMapper mapper;

    @PostMapping("/types/")
    public ObjectNode createType(@Validated @RequestBody Type type) {

        ObjectNode data = mapper.createObjectNode();
        ObjectNode response = mapper.createObjectNode();

        try {
            Type instance = typeRepository.save(type);
            data.put("id", instance.getId());

            response.put("result" , "ok");
            response.set("data", data);

        } catch (Exception e) {
            response.put("result" , "error");
            response.put("message", "Unable to save new instance with the data provided");
        }

        return response;
    }


    @GetMapping("/types/")
    public ObjectNode readTypes() {
        List<Type> types  = typeRepository.findAll();
        ObjectNode response = mapper.createObjectNode();

        response.put("result" , "ok");
        response.set("data", mapper.convertValue(types, JsonNode.class));

        return response;
    }

    @GetMapping("/types/{id}")
    public ObjectNode readType(@PathVariable("id") Integer id) {

        ObjectNode response = mapper.createObjectNode();
        Optional<Type> type = typeRepository.findById(id);

        if (type.isPresent()) {
            response.put("result" , "ok");
            response.set("data", mapper.convertValue(type.get(), JsonNode.class));
        } else {
            response.put("result" , "error");
            response.put("message", "Unable to find instance with the ID provided");
        }

        return response;
    }

    @PutMapping("/types/{id}")
    public ObjectNode updateType(@PathVariable("id") Integer id, @Validated @RequestBody Type type) {

        ObjectNode response = mapper.createObjectNode();
        Optional<Type> results = typeRepository.findById(id);

        if(results.isPresent()) {
            type.setId(id);
            typeRepository.save(type);

            response.put("result" , "ok");
            response.put("message", "Successfully updated instance");
        }
        else {
            response.put("result" , "error");
            response.put("message", "Unable to find requested instance");
        }

        return response;
    }

    @DeleteMapping("/types/{id}")
    public ObjectNode deleteType(@PathVariable("id") Integer id) {

        ObjectNode response = mapper.createObjectNode();
        Optional<Type> type = typeRepository.findById(id);

        if(type.isPresent()) {
            typeRepository.deleteById(id);

            response.put("result" , "ok");
            response.put("message", "Successfully deleted instance");
        } else {
            response.put("result" , "error");
            response.put("message", "Unable to find requested instance");
        }

        return response;
    }
}
