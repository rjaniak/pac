package com.prodyna.conference.backend.service;

import com.prodyna.conference.backend.model.Location;
import com.prodyna.conference.backend.repository.LocationRepository;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LocationService {

    @Autowired
    LocationRepository locationRepository;

    public Location addLocation(Location location) {
        return locationRepository.save(location);
    }

    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    public Location getLocation(Long id) {
        Optional<Location> location = locationRepository.findById(id);
        if (!location.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Location found with id [" + id + "].");
        }
        return location.get();
    }

    public List<Location> getLocations() {
        List<Location> locations = new ArrayList<>();
        Iterable<Location> allLocations = locationRepository.findAll();
        allLocations.forEach(locations::add);
        return locations;
    }

    public Location updateLocation(Long id, Location location) {
        Optional<Location> oldLocation = locationRepository.findById(id);
        if (!oldLocation.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Location found with id [" + id + "].");
        }
        location.setId(id);
        return locationRepository.save(location);
    }
}
