package org.kodigo.agenda.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.kodigo.agenda.entities.Label;
import org.kodigo.agenda.repositories.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class LabelsController {

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ObjectMapper mapper;

    @PostMapping("/labels/")
    public ObjectNode createLabel(@Validated @RequestBody Label label) {

        ObjectNode data =  mapper.createObjectNode();
        ObjectNode response =  mapper.createObjectNode();

        try {
            Label instance = labelRepository.save(label);
            data.put("id", instance.getId());

            response.put("result" , "ok");
            response.set("data", data);

        } catch (Exception e) {
            response.put("result" , "error");
            response.put("message", "Unable to save new instance with the data provided");
        }

        return response;
    }


    @GetMapping("/labels/")
    public ObjectNode readLabels() {
        List<Label> labels  = labelRepository.findAll();
        ObjectNode response = mapper.createObjectNode();

        response.put("result" , "ok");
        response.set("data", mapper.convertValue(labels, JsonNode.class));

        return response;
    }


    @GetMapping("/labels/{id}")
    public ObjectNode readLabel(@PathVariable("id") Integer id) {

        ObjectNode response   = mapper.createObjectNode();
        Optional<Label> label = labelRepository.findById(id);

        if (label.isPresent()) {
            response.put("result" , "ok");
            response.set("data", mapper.convertValue(label.get(), JsonNode.class));
        } else {
            response.put("result" , "error");
            response.put("message", "Unable to find instance with the ID provided");
        }

        return response;
    }


    @PutMapping("/labels/{id}")
    public ObjectNode updateLabel(@PathVariable("id") Integer id, @Validated @RequestBody Label label) {

        ObjectNode response = mapper.createObjectNode();
        Optional<Label> results = labelRepository.findById(id);

        if(results.isPresent()) {
            label.setId(id);
            labelRepository.save(label);

            response.put("result" , "ok");
            response.put("message", "Successfully updated instance");
        }
        else {
            response.put("result" , "error");
            response.put("message", "Unable to find requested instance");
        }

        return response;
    }


    @DeleteMapping("/labels/{id}")
    public ObjectNode deleteLabel(@PathVariable("id") Integer id) {

        ObjectNode response = mapper.createObjectNode();
        Optional<Label> label = labelRepository.findById(id);

        if(label.isPresent()) {
            labelRepository.deleteById(id);

            response.put("result" , "ok");
            response.put("message", "Successfully deleted instance");
        } else {
            response.put("result" , "error");
            response.put("message", "Unable to find requested instance");
        }

        return response;
    }
}
