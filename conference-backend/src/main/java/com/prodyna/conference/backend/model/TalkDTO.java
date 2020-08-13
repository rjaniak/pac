package com.prodyna.conference.backend.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Data
public class TalkDTO {
    @NotBlank(message = "Talk id is mandatory")
    private String talkId;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @Pattern(regexp = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$",
            message = "Date must have pattern yyyy-MM-dd")
    private String date;

    @Pattern(regexp = "^([2][0-3]|[0-1][0-9]|[1-9]):[0-5][0-9]$",
            message = "Start time must have pattern HH:mm")
    private String startTime;

    @Min(5)
    @Max(1440)
    private int duration;

    private String language;

    private String level;

    @NotNull(message = "Event ID is mandatory")
    String eventId;

    List<String> personIds;

    @NotNull(message = "Room ID is mandatory")
    String roomId;

    List<String> topicIds;
}
