package com.prodyna.conference.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.*;

import java.util.List;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Data
@NodeEntity
public class Event {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String begin;

    private String end;

    @JsonIgnoreProperties({"events","rooms"})
    @EqualsAndHashCode.Exclude
    @Relationship(type = "TAKES_PLACE_IN")
    private Location location;

    @JsonIgnoreProperties({"event", "persons", "room", "timeSlot", "topics"})
    @EqualsAndHashCode.Exclude
    @Relationship(type = "PART_OF", direction = Relationship.INCOMING)
    private List<Talk> talks;
}