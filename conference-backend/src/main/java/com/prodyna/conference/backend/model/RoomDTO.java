package com.prodyna.conference.backend.model;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Data
public class RoomDTO {
    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Location ID is mandatory")
    Long locationId;
}
