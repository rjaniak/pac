package com.prodyna.conference.backend.service;

import com.prodyna.conference.backend.model.Talk;
import com.prodyna.conference.backend.model.TimeSlot;
import com.prodyna.conference.backend.repository.TimeSlotRepository;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Service
@Transactional
@Timed
public class TimeSlotService {

    @Autowired
    TimeSlotRepository timeSlotRepository;

    public TimeSlot addTimeSlot(Talk talk, LocalTime startTime, LocalTime endTime) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setDate(talk.getDate());
        timeSlot.setStartTime(startTime.format(DateTimeFormatter.ISO_TIME));
        timeSlot.setEndTime(endTime.format(DateTimeFormatter.ISO_TIME));
        timeSlot.setTalk(talk);
        timeSlot.setRoom(talk.getRoom());
        String timeSlotId = timeSlot.getRoom().getRoomId() + "-" + timeSlot.getDate() + "-"
                + timeSlot.getStartTime();
        timeSlot.setTimeSlotId(timeSlotId);
        return timeSlotRepository.save(timeSlot);
    }

    public TimeSlot updateTimeSlot(Talk talk, LocalTime startTime, LocalTime endTime) {
        TimeSlot timeSlot = talk.getTimeSlot();
        timeSlot.setDate(talk.getDate());
        timeSlot.setStartTime(startTime.format(DateTimeFormatter.ISO_TIME));
        timeSlot.setEndTime(endTime.format(DateTimeFormatter.ISO_TIME));
        timeSlot.setRoom(talk.getRoom());
        String timeSlotId = timeSlot.getRoom().getRoomId() + "-" + timeSlot.getDate() + "-"
                + timeSlot.getStartTime();
        timeSlot.setTimeSlotId(timeSlotId);
        return timeSlotRepository.save(timeSlot);
    }
}
