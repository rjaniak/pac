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
        if (talkRepository.existsByTalkId(talkDTO.getTalkId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Talk with [" + talkDTO.getTalkId() + "] already exists.");
        }
        Talk talk = mapToTalk(talkDTO, new Talk());
        talk = addTimeSlot(talk, false);
        return talkRepository.save(talk);
    }

    public void deleteTalk(String talkId) {
        Optional<Talk> talkOptional = talkRepository.findByTalkId(talkId);
        if (talkOptional.isPresent()) {
            Talk talk = talkOptional.get();
            timeSlotRepository.delete(talk.getTimeSlot());
            talkRepository.deleteByTalkId(talkId);
        }
    }

    public Talk getTalk(String id) {
        Optional<Talk> talk = talkRepository.findByTalkId(id);
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

    public Talk updateTalk(String id, TalkDTO talkDTO) {
        Optional<Talk> talkOptional = talkRepository.findByTalkId(id);
        if (!talkOptional.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Talk found with id [" + id + "].");
        }
        Talk talk = mapToTalk(talkDTO, talkOptional.get());
        talk = addTimeSlot(talk, true);
        return talkRepository.save(talk);
    }

    private Talk addEvent(Talk talk, String eventId) {
        Optional<Event> event = eventRepository.findByEventId(eventId);
        if (event.isPresent()) {
            talk.setEvent(event.get());
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "A talk must have an event. No Event found with id [" + eventId + "].");
        }
        return talk;
    }

    private Talk addPersons(Talk talk, List<String> personIds) {
        Iterable<Person> persons = personRepository.findAllByPersonIds(personIds);
        List<Person> personList = new ArrayList<>();
        persons.forEach(personList::add);
        talk.setPersons(personList);
        return talk;
    }

    private Talk addRoom(Talk talk, String roomId) {
        Optional<Room> room = roomRepository.findByRoomId(roomId);
        if (room.isPresent()) {
            talk.setRoom(room.get());
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "A talk must have an room. No Room found with id [" + roomId + "].");
        }
        return talk;
    }

    private Talk addTopics(Talk talk, List<String> topicIds) {
        Iterable<Topic> topics = topicRepository.findAllByTopicIds(topicIds);
        List<Topic> topicList = new ArrayList<>();
        topics.forEach(topicList::add);
        talk.setTopics(topicList);
        return talk;
    }

    private Talk mapToTalk(TalkDTO talkDTO, Talk talk) {
        talk.setTalkId(talkDTO.getTalkId());
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
        return talk;
    }

    private Talk addTimeSlot(Talk talk, boolean onlyUpdate) {
        LocalTime startTime = LocalTime.parse(talk.getStartTime(), DateTimeFormatter.ISO_TIME);
        LocalTime endTime = startTime.plusMinutes(talk.getDuration());
        if (!roomService.isRoomAvailable(talk, startTime, endTime)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "The requested time slot is already blocked. Please choose different time.");
        }
        TimeSlot timeSlot;
        if (onlyUpdate) {
            timeSlot = timeSlotService.updateTimeSlot(talk, startTime, endTime);
        } else {
            timeSlot = timeSlotService.addTimeSlot(talk, startTime, endTime);
        }
        talk.setTimeSlot(timeSlot);
        return talk;
    }
}
