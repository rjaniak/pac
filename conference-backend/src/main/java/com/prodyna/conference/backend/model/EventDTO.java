package com.prodyna.conference.backend.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Data
public class EventDTO {
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Pattern(regexp = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$",
            message = "Start date must have pattern yyyy-MM-dd")
    private String begin;

    @Pattern(regexp = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$",
            message = "End date must have pattern yyyy-MM-dd")
    private String end;

    @NotNull(message = "Location ID is mandatory")
    Long locationId;
}
