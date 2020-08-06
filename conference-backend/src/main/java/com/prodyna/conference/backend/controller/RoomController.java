package com.prodyna.conference.backend.controller;

import com.prodyna.conference.backend.model.Room;
import com.prodyna.conference.backend.model.RoomDTO;
import com.prodyna.conference.backend.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    RoomService roomService;

    @GetMapping
    public List<Room> getRooms() {
        return roomService.getRooms();
    }

    @GetMapping("{id}")
    public Room getRoom(@PathVariable Long id) {
        return roomService.getRoom(id);
    }

    @PostMapping
    public Room addRoom(@Valid @RequestBody RoomDTO roomDTO, Errors errors) {
        validate(roomDTO, errors);
        return roomService.addRoom(roomDTO);
    }

    @PutMapping("{id}")
    public Room updateRoom(@PathVariable Long id, @Valid @RequestBody RoomDTO roomDTO, Errors errors) {
        validate(roomDTO, errors);
        return roomService.updateRoom(id, roomDTO);
    }

    @DeleteMapping("{id}")
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }

    private void validate(RoomDTO roomDTO, Errors errors) {
        if (errors.hasErrors()) {
            String errorMsg = errors.getAllErrors()
                    .stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining(", "));
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, errorMsg);
        }
    }
}
