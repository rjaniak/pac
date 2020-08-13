package com.prodyna.conference.backend.controller;

import com.prodyna.conference.backend.model.Location;
import com.prodyna.conference.backend.model.LocationDTO;
import com.prodyna.conference.backend.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
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
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    LocationService locationService;

    @GetMapping
    public List<Location> getLocations() {
        return locationService.getLocations();
    }

    @GetMapping("{id}")
    public Location getLocation(@PathVariable String id) {
        return locationService.getLocation(id);
    }

    @PostMapping
    @Secured("ROLE_USER")
    public Location addLocation(@Valid @RequestBody LocationDTO locationDTO, Errors errors) {
        validate(locationDTO, errors);
        return locationService.addLocation(locationDTO);
    }

    @PutMapping("{id}")
    @Secured("ROLE_USER")
    public Location updateLocation(@PathVariable String id, @Valid @RequestBody LocationDTO locationDTO, Errors errors) {
        validate(locationDTO, errors);
        return locationService.updateLocation(id, locationDTO);
    }

    @DeleteMapping("{id}")
    @Secured("ROLE_USER")
    public void deleteLocation(@PathVariable String id) {
        locationService.deleteLocation(id);
    }

    private void validate(LocationDTO locationDTO, Errors errors) {
        if (errors.hasErrors()) {
            String errorMsg = errors.getAllErrors()
                    .stream().map(error -> error.getDefaultMessage()).collect(Collectors.joining(", "));
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, errorMsg);
        }
    }
}
