package com.prodyna.conference.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.neo4j.ogm.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Data
public class TopicDTO {
    @NotBlank(message = "Name is mandatory")
    private String name;

    List<Long> parentTopicIds;
}
