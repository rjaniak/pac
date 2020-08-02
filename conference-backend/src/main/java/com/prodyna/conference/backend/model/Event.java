package com.prodyna.conference.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Getter
@Setter
@NodeEntity
public class Event {
    @Id
    @GeneratedValue
    private Long id;

    @Index(unique = true)
    private String name;

    private String begin;

    private String end;

    @Relationship(type = "TAKES_PLACE_IN")
    private TakesPlaceInRelationship takesPlaceInRelationship;
}
