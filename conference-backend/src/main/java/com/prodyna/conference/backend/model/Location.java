package com.prodyna.conference.backend.model;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

import java.util.List;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Getter
@Setter
@NodeEntity
public class Location {
    @Id
    @GeneratedValue
    private Long id;

    @Index(unique = true)
    private String name;

    @Relationship(type = "TAKES_PLACE_IN", direction = Relationship.INCOMING)
    private List<TakesPlaceInRelationship> eventTakesPlaceInLocationRelationships;
}
