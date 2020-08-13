package com.prodyna.conference.backend.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Data
public class RoomDTO {
    @NotBlank(message = "Room id is mandatory")
    private String roomId;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Location ID is mandatory")
    String locationId;
}
