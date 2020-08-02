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
public class EventDTO {
    private String name;
    private String begin;
    private String end;
    Long locationId;
}
