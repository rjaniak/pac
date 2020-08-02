package com.prodyna.conference.backend.controller;

import com.prodyna.conference.backend.model.Location;
import com.prodyna.conference.backend.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Location getLocation(@PathVariable Long id) {
        return locationService.getLocation(id);
    }

    @PostMapping
    public Location addLocation(@RequestBody Location location) {
        return locationService.addLocation(location);
    }

    @PutMapping("{id}")
    public Location updateLocation(@PathVariable Long id, @RequestBody Location location) {
        return locationService.updateLocation(id, location);
    }

    @DeleteMapping("{id}")
    public void deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
    }
}
