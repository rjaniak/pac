package com.prodyna.conference.backend.model;

import lombok.*;
import org.neo4j.ogm.annotation.*;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Setter
@RelationshipEntity(type = "TAKES_PLACE_IN")
public class TakesPlaceInRelationship {
    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    @ToString.Exclude
    private Event event;

    @EndNode
    @ToString.Exclude
    private Location location;
}
