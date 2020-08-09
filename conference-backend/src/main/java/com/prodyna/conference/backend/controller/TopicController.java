package com.prodyna.conference.backend.controller;

import com.prodyna.conference.backend.model.Topic;
import com.prodyna.conference.backend.model.TopicDTO;
import com.prodyna.conference.backend.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    TopicService topicService;

    @GetMapping
    public List<Topic> getTopics() {
        return topicService.getTopics();
    }

    @GetMapping("{id}")
    public Topic getTopic(@PathVariable Long id) {
        return topicService.getTopic(id);
    }

    @PostMapping
    @Secured("ROLE_USER")
    public Topic addTopic(@Valid @RequestBody TopicDTO topicDTO, Errors errors) {
        validate(topicDTO, errors);
        return topicService.addTopic(topicDTO);
    }

    @PutMapping("{id}")
    @Secured("ROLE_USER")
    public Topic updateTopic(@PathVariable Long id, @Valid @RequestBody TopicDTO topicDTO, Errors errors) {
        validate(topicDTO, errors);
        return topicService.updateTopic(id, topicDTO);
    }

    @DeleteMapping("{id}")
    @Secured("ROLE_USER")
    public void deleteTopic(@PathVariable Long id) {
        topicService.deleteTopic(id);
    }

    private void validate(TopicDTO topicDTO, Errors errors) {
        if (errors.hasErrors()) {
            String errorMsg = errors.getAllErrors()
                    .stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining(", "));
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, errorMsg);
        }
    }
}
