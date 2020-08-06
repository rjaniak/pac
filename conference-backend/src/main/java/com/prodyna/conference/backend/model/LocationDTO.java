package com.prodyna.conference.backend.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Data
public class LocationDTO {
    @NotBlank(message = "Name is mandatory")
    private String name;
}