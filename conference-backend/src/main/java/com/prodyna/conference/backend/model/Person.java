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
public class Person {
    @Id
    @GeneratedValue
    private Long id;

    private String personId;

    private String name;

    @JsonIgnoreProperties("persons")
    @EqualsAndHashCode.Exclude
    @Relationship(type = "BELONGS_TO")
    private Organization organization;

    @JsonIgnoreProperties({"event", "persons", "room", "timeSlot", "topics"})
    @EqualsAndHashCode.Exclude
    @Relationship(type = "HELD_BY", direction = Relationship.INCOMING)
    private List<Talk> talks;
}
