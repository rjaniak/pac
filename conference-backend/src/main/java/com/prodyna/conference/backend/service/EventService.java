package com.prodyna.conference.backend.service;

import com.prodyna.conference.backend.model.*;
import com.prodyna.conference.backend.repository.EventRepository;
import com.prodyna.conference.backend.repository.LocationRepository;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
        if (eventRepository.existsByEventId(eventDTO.getEventId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Event with [" + eventDTO.getEventId() + "] already exists.");
        }
        Event event = mapToEvent(eventDTO, new Event());
        return eventRepository.save(event);
    }

    public void deleteEvent(String id) {
        eventRepository.deleteByEventId(id);
    }

    public Event getEvent(String id) {
        Optional<Event> event = eventRepository.findByEventId(id);
        if (!event.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Event found with id [" + id + "].");
        }
        return event.get();
    }

    public List<Event> getEvents() {
        List<Event> events = new ArrayList<>();
        Iterable<Event> allEvents = eventRepository.findAll(Sort.by("begin"));
        allEvents.forEach(events::add);
        return events;
    }

    public Event updateEvent(String id, EventDTO eventDTO) {
        Optional<Event> eventOptional = eventRepository.findByEventId(id);
        if (!eventOptional.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Event found with id [" + id + "].");
        }
        Event event = mapToEvent(eventDTO, eventOptional.get());
        return eventRepository.save(event);
    }

    private Event addLocation(Event event, String locationId) {
        Optional<Location> location = locationRepository.findByLocationId(locationId);
        if (location.isPresent()) {
            event.setLocation(location.get());
        } else {
            event.setLocation(null);
        }
        return event;
    }

    private Event mapToEvent(EventDTO eventDTO, Event event) {
        event.setEventId(eventDTO.getEventId());
        event.setName(eventDTO.getName());
        event.setBegin(eventDTO.getBegin());
        event.setEnd(eventDTO.getEnd());
        event = addLocation(event, eventDTO.getLocationId());
        return event;
    }
}
