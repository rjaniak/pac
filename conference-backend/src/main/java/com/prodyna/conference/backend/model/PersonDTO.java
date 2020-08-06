package com.prodyna.conference.backend.model;

import lombok.Data;
import org.neo4j.ogm.annotation.NodeEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Data
public class PersonDTO {
    @NotBlank(message = "Name is mandatory")
    private String name;

    Long organizationId;
}
