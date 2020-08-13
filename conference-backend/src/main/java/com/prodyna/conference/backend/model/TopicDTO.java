package com.prodyna.conference.backend.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Data
public class TopicDTO {
    @NotBlank(message = "Topic id is mandatory")
    private String topicId;

    @NotBlank(message = "Name is mandatory")
    private String name;

    List<String> parentTopicIds;
}
