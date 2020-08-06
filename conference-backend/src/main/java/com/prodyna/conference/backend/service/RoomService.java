package com.prodyna.conference.backend.service;

import com.prodyna.conference.backend.model.Location;
import com.prodyna.conference.backend.model.Room;
import com.prodyna.conference.backend.model.RoomDTO;
import com.prodyna.conference.backend.model.TimeSlot;
import com.prodyna.conference.backend.repository.LocationRepository;
import com.prodyna.conference.backend.repository.RoomRepository;
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

    public Room addRoom(RoomDTO roomDTO) {
        Room room = mapToRoom(roomDTO, new Room());
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    public Room getRoom(Long id) {
        Optional<Room> room = roomRepository.findById(id);
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

    public boolean isRoomAvailable(Room room, String date, LocalTime startTime, LocalTime endTime) {
        boolean available = true;
        List<TimeSlot> blockedTimeSlots = room.getTimeSlots();
        for (TimeSlot blockedTimeSlot : blockedTimeSlots) {
            if (hasConflict(blockedTimeSlot, date, startTime, endTime)) {
                available = false;
                break;
            }
        }
        return available;
    }

    public Room updateRoom(Long id, RoomDTO roomDTO) {
        Optional<Room> roomOptional = roomRepository.findById(id);
        if (!roomOptional.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Room found with id [" + id + "].");
        }
        Room room = mapToRoom(roomDTO, roomOptional.get());
        return roomRepository.save(room);
    }

    private Room addLocation(Room room, Long locationId) {
        Optional<Location> location = locationRepository.findById(locationId);
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
            return true;
        } else {
            return false;
        }
    }

    private Room mapToRoom(RoomDTO roomDTO, Room room) {
        room.setName(roomDTO.getName());
        room = addLocation(room, roomDTO.getLocationId());
        return room;
    }
}
