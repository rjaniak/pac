package com.prodyna.conference.backend.service;

import com.prodyna.conference.backend.model.*;
import com.prodyna.conference.backend.repository.LocationRepository;
import com.prodyna.conference.backend.repository.RoomRepository;
import com.prodyna.conference.backend.repository.TimeSlotRepository;
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
public class RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    TimeSlotRepository timeSlotRepository;

    public Room addRoom(RoomDTO roomDTO) {
        if (roomRepository.existsByRoomId(roomDTO.getRoomId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Room with [" + roomDTO.getRoomId() + "] already exists.");
        }
        Room room = mapToRoom(roomDTO, new Room());
        return roomRepository.save(room);
    }

    public void deleteRoom(String id) {
        roomRepository.deleteByRoomId(id);
    }

    public Room getRoom(String id) {
        Optional<Room> room = roomRepository.findByRoomId(id);
        if (!room.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Room found with id [" + id + "].");
        }
        return room.get();
    }

    public List<Room> getRooms() {
        List<Room> rooms = new ArrayList<>();
        Iterable<Room> allRooms = roomRepository.findAll();
        allRooms.forEach(rooms::add);
        return rooms;
    }

    public boolean isRoomAvailable(Talk talk, LocalTime startTime, LocalTime endTime) {
        boolean available = true;
        List<TimeSlot> blockedTimeSlots = talk.getRoom().getTimeSlots();
        for (TimeSlot blockedTimeSlot : blockedTimeSlots) {
            Optional<TimeSlot> timeSlotOptional =
                    timeSlotRepository.findByTimeSlotId(blockedTimeSlot.getTimeSlotId());
            if (timeSlotOptional.isEmpty()) {
                continue;
            }
            if (timeSlotOptional.get().getTalk().getTalkId().equals(talk.getTalkId())) {
                // This timeSlot is from current talk, so ignore it
                continue;
            }
            if (hasConflict(blockedTimeSlot, talk.getDate(), startTime, endTime)) {
                available = false;
                break;
            }
        }
        return available;
    }

    public Room updateRoom(String id, RoomDTO roomDTO) {
        Optional<Room> roomOptional = roomRepository.findByRoomId(id);
        if (!roomOptional.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Room found with id [" + id + "].");
        }
        Room room = mapToRoom(roomDTO, roomOptional.get());
        return roomRepository.save(room);
    }

    private Room addLocation(Room room, String locationId) {
        Optional<Location> location = locationRepository.findByLocationId(locationId);
        if (location.isPresent()) {
            room.setLocation(location.get());
        } else {
            room.setLocation(null);
        }
        return room;
    }

    private boolean hasConflict(TimeSlot timeSlot, String date, LocalTime startTime, LocalTime endTime) {
        if (!timeSlot.getDate().equals(date)) {
            return false;
        }
        LocalTime blockedTimeSlotStart = LocalTime.parse(timeSlot.getStartTime(), DateTimeFormatter.ISO_TIME);
        LocalTime blockedTimeSlotEnd = LocalTime.parse(timeSlot.getEndTime(), DateTimeFormatter.ISO_TIME);

        if (endTime.compareTo(blockedTimeSlotStart) <= 0 || startTime.compareTo(blockedTimeSlotEnd) >= 0) {
            return false;
        } else {
            return true;
        }
    }

    private Room mapToRoom(RoomDTO roomDTO, Room room) {
        room.setRoomId(roomDTO.getRoomId());
        room.setName(roomDTO.getName());
        room = addLocation(room, roomDTO.getLocationId());
        return room;
    }
}
