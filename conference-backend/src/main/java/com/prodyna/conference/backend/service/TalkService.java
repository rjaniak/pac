package com.prodyna.conference.backend.service;

import com.prodyna.conference.backend.model.*;
import com.prodyna.conference.backend.repository.*;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Service
@Transactional
@Timed
public class TalkService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomService roomService;

    @Autowired
    TalkRepository talkRepository;

    @Autowired
    TimeSlotRepository timeSlotRepository;

    @Autowired
    TimeSlotService timeSlotService;

    @Autowired
    TopicRepository topicRepository;

    public Talk addTalk(TalkDTO talkDTO) {
        Talk talk = mapToTalk(talkDTO, new Talk());
        return talkRepository.save(talk);
    }

    public void deleteTalk(Long talkId) {
        Optional<Talk> talkOptional = talkRepository.findById(talkId);
        if (talkOptional.isPresent()) {
            Talk talk = talkOptional.get();
            timeSlotRepository.delete(talk.getTimeSlot());
            talkRepository.deleteById(talkId);
        }
    }

    public Talk getTalk(Long id) {
        Optional<Talk> talk = talkRepository.findById(id);
        if (!talk.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Talk found with id [" + id + "].");
        }
        return talk.get();
    }

    public List<Talk> getTalks() {
        List<Talk> talks = new ArrayList<>();
        Iterable<Talk> allTalks = talkRepository.findAll();
        allTalks.forEach(talks::add);
        return talks;
    }

    public Talk updateTalk(Long id, TalkDTO talkDTO) {
        Optional<Talk> talkOptional = talkRepository.findById(id);
        if (!talkOptional.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Talk found with id [" + id + "].");
        }
        Talk talk = mapToTalk(talkDTO, talkOptional.get());
        return talkRepository.save(talk);
    }

    private Talk addEvent(Talk talk, Long eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isPresent()) {
            talk.setEvent(event.get());
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "A talk must have an event. No Event found with id [" + eventId + "].");
        }
        return talk;
    }

    private Talk addPersons(Talk talk, List<Long> personIds) {
        Iterable<Person> persons = personRepository.findAllById(personIds);
        List<Person> personList = new ArrayList<>();
        persons.forEach(personList::add);
        talk.setPersons(personList);
        return talk;
    }

    private Talk addRoom(Talk talk, Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        if (room.isPresent()) {
            talk.setRoom(room.get());
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "A talk must have an room. No Room found with id [" + roomId + "].");
        }
        return talk;
    }

    private Talk addTopics(Talk talk, List<Long> topicIds) {
        Iterable<Topic> topics = topicRepository.findAllById(topicIds);
        List<Topic> topicList = new ArrayList<>();
        topics.forEach(topicList::add);
        talk.setTopics(topicList);
        return talk;
    }

    private Talk mapToTalk(TalkDTO talkDTO, Talk talk) {
        talk.setTitle(talkDTO.getTitle());
        talk.setDate(talkDTO.getDate());
        talk.setStartTime(talkDTO.getStartTime());
        talk.setDuration(talkDTO.getDuration());
        talk.setLanguage(talkDTO.getLanguage());
        talk.setLevel(talkDTO.getLevel());
        talk = addEvent(talk, talkDTO.getEventId());
        talk = addPersons(talk, talkDTO.getPersonIds());
        talk = addRoom(talk, talkDTO.getRoomId());
        talk = addTopics(talk, talkDTO.getTopicIds());
        talk = addTimeSlot(talk);
        return talk;
    }

    private Talk addTimeSlot(Talk talk) {
        LocalTime startTime = LocalTime.parse(talk.getStartTime(), DateTimeFormatter.ISO_TIME);
        LocalTime endTime = startTime.plusMinutes(talk.getDuration());
        if (!roomService.isRoomAvailable(talk.getRoom(), talk.getDate(), startTime, endTime)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "The requested time slot is already blocked. Please choose different time.");
        }
        TimeSlot timeSlot = timeSlotService.addTimeSlot(talk, startTime, endTime);
        talk.setTimeSlot(timeSlot);
        return talk;
    }
}
