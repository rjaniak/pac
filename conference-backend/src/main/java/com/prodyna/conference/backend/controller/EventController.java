package com.prodyna.conference.backend.controller;

import com.prodyna.conference.backend.model.Event;
import com.prodyna.conference.backend.model.EventDTO;
import com.prodyna.conference.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    EventService eventService;

    @GetMapping
    public List<Event> getEvents() {
        return eventService.getEvents();
    }

    @GetMapping("{id}")
    public Event getEvent(@PathVariable Long id) {
        return eventService.getEvent(id);
    }

    @PostMapping
    public Event addEvent(@RequestBody EventDTO eventDTO) {
        return eventService.addEvent(eventDTO);
    }

    @PutMapping("{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody EventDTO eventDTO) {
        return eventService.updateEvent(id, eventDTO);
    }

    @DeleteMapping("{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }
}
