package com.prodyna.conference.backend.service;

import com.prodyna.conference.backend.model.Event;
import com.prodyna.conference.backend.model.EventDTO;
import com.prodyna.conference.backend.model.Location;
import com.prodyna.conference.backend.model.LocationDTO;
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

    public Location addLocation(LocationDTO locationDTO) {
        Location location = new Location();
        location.setName(locationDTO.getName());
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

    public Location updateLocation(Long id, LocationDTO locationDTO) {
        Optional<Location> locationOptional = locationRepository.findById(id);
        if (!locationOptional.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Location found with id [" + id + "].");
        }
        Location location = locationOptional.get();
        location.setName(locationDTO.getName());
        return locationRepository.save(location);
    }
}
