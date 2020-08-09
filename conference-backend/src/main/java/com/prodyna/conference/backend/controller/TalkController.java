package com.prodyna.conference.backend.controller;

import com.prodyna.conference.backend.model.Talk;
import com.prodyna.conference.backend.model.TalkDTO;
import com.prodyna.conference.backend.service.TalkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@RestController
@RequestMapping("/api/talks")
public class TalkController {

    @Autowired
    TalkService talkService;

    @GetMapping
    public List<Talk> getTalks() {
        return talkService.getTalks();
    }

    @GetMapping("{id}")
    public Talk getTalk(@PathVariable Long id) {
        return talkService.getTalk(id);
    }

    @PostMapping
    @Secured("ROLE_USER")
    public Talk addTalk(@Valid @RequestBody TalkDTO talkDTO, Errors errors) {
        validate(talkDTO, errors);
        return talkService.addTalk(talkDTO);
    }

    @PutMapping("{id}")
    @Secured("ROLE_USER")
    public Talk updateTalk(@PathVariable Long id, @Valid @RequestBody TalkDTO talkDTO, Errors errors) {
        validate(talkDTO, errors);
        return talkService.updateTalk(id, talkDTO);
    }

    @DeleteMapping("{id}")
    @Secured("ROLE_USER")
    public void deleteTalk(@PathVariable Long id) {
        talkService.deleteTalk(id);
    }

    private void validate(TalkDTO talkDTO, Errors errors) {
        if (!errors.hasErrors()) {
            // Input is basically correct, but now perform specific validation
            try {
                LocalTime startTime = LocalTime.parse(talkDTO.getStartTime(), DateTimeFormatter.ISO_TIME);
                int duration = talkDTO.getDuration();
                LocalTime endTime = startTime.plusMinutes(duration);
                if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
                    errors.reject("time.invalid", "Talks until after midnight are not allowed");
                }
            } catch (DateTimeParseException e) {
                errors.reject("startTime.invalid", "Exception while parsing date: " + e.getMessage());
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
