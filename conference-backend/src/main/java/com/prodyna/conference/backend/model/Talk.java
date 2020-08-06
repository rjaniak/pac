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
public class Talk {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String date;
    private String startTime;
    // Duration in minutes
    private int duration;
    private String language;
    private String level;

    @JsonIgnoreProperties({"location", "talks"})
    @EqualsAndHashCode.Exclude
    @Relationship(type = "PART_OF")
    private Event event;

    @JsonIgnoreProperties({"organization", "talks"})
    @EqualsAndHashCode.Exclude
    @Relationship(type = "HELD_BY")
    private List<Person> persons;

    @JsonIgnoreProperties({"timeSlots", "location", "talks"})
    @EqualsAndHashCode.Exclude
    @Relationship(type = "HELD_IN")
    private Room room;

    @JsonIgnoreProperties({"talk", "room"})
    @EqualsAndHashCode.Exclude
    @Relationship(type = "HAS")
    private TimeSlot timeSlot;

    @JsonIgnoreProperties({"childTopics", "parentTopics", "talks"})
    @EqualsAndHashCode.Exclude
    @Relationship(type = "IS_ABOUT")
    private List<Topic> topics;
}
