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
public class Room {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @JsonIgnoreProperties({"talk", "room"})
    @EqualsAndHashCode.Exclude
    @Relationship(type = "BLOCKS", direction = Relationship.INCOMING)
    private List<TimeSlot> timeSlots;

    @JsonIgnoreProperties({"events","rooms"})
    @EqualsAndHashCode.Exclude
    @Relationship(type = "IS_IN")
    private Location location;

    @JsonIgnoreProperties({"event", "persons", "room", "timeSlot", "topics"})
    @EqualsAndHashCode.Exclude
    @Relationship(type = "HELD_IN", direction = Relationship.INCOMING)
    private List<Talk> talks;
}
