package com.prodyna.conference.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Data
@NodeEntity
public class TimeSlot {
    @Id
    @GeneratedValue
    private Long id;

    private String timeSlotId;

    private String date;
    private String startTime;
    private String endTime;

    @JsonIgnoreProperties({"event", "persons", "room", "timeSlot", "topics"})
    @EqualsAndHashCode.Exclude
    @Relationship(type = "HAS", direction = Relationship.INCOMING)
    private Talk talk;

    @JsonIgnoreProperties({"timeSlots", "location", "talks"})
    @EqualsAndHashCode.Exclude
    @Relationship(type = "BLOCKS")
    private Room room;
}
