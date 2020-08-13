package com.prodyna.conference.backend.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Data
public class PersonDTO {
    @NotBlank(message = "Person id is mandatory")
    private String personId;

    @NotBlank(message = "Name is mandatory")
    private String name;

    String organizationId;
}
