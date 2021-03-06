package com.prodyna.conference.backend.controller;

import com.prodyna.conference.backend.model.Event;
import com.prodyna.conference.backend.model.EventDTO;
import com.prodyna.conference.backend.model.Talk;
import com.prodyna.conference.backend.model.Topic;
import com.prodyna.conference.backend.service.EventService;
import com.prodyna.conference.backend.service.TalkService;
import com.prodyna.conference.backend.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    EventService eventService;

    @Autowired
    TalkService talkService;

    @Autowired
    TopicService topicService;

    @GetMapping
    public List<Event> getEvents() {
        return eventService.getEvents();
    }

    @GetMapping("{id}")
    public Event getEvent(@PathVariable String id) {
        return eventService.getEvent(id);
    }

    @GetMapping("{id}/talks")
    public List<Talk> getEventTalks(@PathVariable String id) {
        return talkService.getTalksByEventId(id);
    }

    @GetMapping("{id}/topics")
    public List<Topic> getEventTopics(@PathVariable String id) {
        return topicService.getTopicsByEventId(id);
    }

    @PostMapping
    @Secured("ROLE_USER")
    public Event addEvent(@Valid @RequestBody EventDTO eventDTO, Errors errors) {
        validate(eventDTO, errors);
        return eventService.addEvent(eventDTO);
    }

    @PutMapping("{id}")
    @Secured("ROLE_USER")
    public Event updateEvent(@PathVariable String id, @Valid @RequestBody EventDTO eventDTO, Errors errors) {
        validate(eventDTO, errors);
        return eventService.updateEvent(id, eventDTO);
    }

    @DeleteMapping("{id}")
    @Secured("ROLE_USER")
    public void deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
    }

    private void validate(EventDTO eventDTO, Errors errors) {
        if (!errors.hasErrors()) {
            // Input is basically correct, but now perform specific validation
            try {
                LocalDate startDate = LocalDate.parse(eventDTO.getBegin(), DateTimeFormatter.ISO_DATE);
                LocalDate endDate = LocalDate.parse(eventDTO.getEnd(), DateTimeFormatter.ISO_DATE);
                if (startDate.isAfter(endDate)) {
                    errors.reject("date.invalid", "The event start date must be before end date");
                }
            } catch (DateTimeParseException e) {
                errors.reject("date.invalid", "Exception while parsing date: " + e.getMessage());
            }
        }

        if (errors.hasErrors()) {
            String errorMsg = errors.getAllErrors()
                    .stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining(", "));
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, errorMsg);
        }
    }
}
