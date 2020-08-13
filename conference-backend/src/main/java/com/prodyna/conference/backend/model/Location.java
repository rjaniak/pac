package com.prodyna.conference.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Data
@NodeEntity
public class Location {
    @Id
    @GeneratedValue
    private Long id;

    private String locationId;

    private String name;

    @JsonIgnoreProperties({"location", "talks"})
    @EqualsAndHashCode.Exclude
    @Relationship(type = "TAKES_PLACE_IN", direction = Relationship.INCOMING)
    private List<Event> events;

    @JsonIgnoreProperties({"timeSlots", "location", "talks"})
    @EqualsAndHashCode.Exclude
    @Relationship(type = "IS_IN", direction = Relationship.INCOMING)
    private List<Room> rooms;
}
