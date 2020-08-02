package com.prodyna.conference.backend.service;

import com.prodyna.conference.backend.model.Event;
import com.prodyna.conference.backend.model.EventDTO;
import com.prodyna.conference.backend.model.Location;
import com.prodyna.conference.backend.model.TakesPlaceInRelationship;
import com.prodyna.conference.backend.repository.EventRepository;
import com.prodyna.conference.backend.repository.LocationRepository;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Service
@Transactional
@Timed
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    LocationRepository locationRepository;

    public Event addEvent(EventDTO eventDTO) {
        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setBegin(eventDTO.getBegin());
        event.setEnd(eventDTO.getEnd());
        Event newEvent = eventRepository.save(event);
        addTakesPlaceInLocation(newEvent, eventDTO.getLocationId());
        return eventRepository.save(newEvent);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public Event getEvent(Long id) {
        Optional<Event> event = eventRepository.findById(id);
        if (!event.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Event found with id [" + id + "].");
        }
        return event.get();
    }

    public List<Event> getEvents() {
        List<Event> events = new ArrayList<>();
        Iterable<Event> allEvents = eventRepository.findAll();
        allEvents.forEach(events::add);
        return events;
    }

    public Event updateEvent(Long id, EventDTO eventDTO) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (!eventOptional.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Event found with id [" + id + "].");
        }
        Event event = eventOptional.get();
        event.setId(id);
        event.setName(eventDTO.getName());
        event.setBegin(eventDTO.getBegin());
        event.setEnd(eventDTO.getEnd());
        addTakesPlaceInLocation(event, eventDTO.getLocationId());
        return eventRepository.save(event);
    }

    private Event addTakesPlaceInLocation(Event event, Long locationId) {
        Optional<Location> locationOptional = locationRepository.findById(locationId);
        if (locationOptional.isPresent()) {
            TakesPlaceInRelationship takesPlaceInRelationship = new TakesPlaceInRelationship();
            takesPlaceInRelationship.setEvent(event);
            takesPlaceInRelationship.setLocation(locationOptional.get());
            event.setTakesPlaceInRelationship(takesPlaceInRelationship);
        } else {
            event.setTakesPlaceInRelationship(null);
        }
        return event;
    }
}
