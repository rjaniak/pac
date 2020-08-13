package com.prodyna.conference.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Data
@NodeEntity
public class Topic {
    @Id
    @GeneratedValue
    private Long id;

    private String topicId;

    private String name;

    @JsonIgnoreProperties({"childTopics", "parentTopics", "talks"})
    @EqualsAndHashCode.Exclude
    @Relationship(type = "RELATED_TO", direction = Relationship.INCOMING)
    private List<Topic> childTopics;

    @JsonIgnoreProperties({"childTopics", "parentTopics", "talks"})
    @EqualsAndHashCode.Exclude
    @Relationship(type = "RELATED_TO")
    private List<Topic> parentTopics;

    @JsonIgnoreProperties({"event", "persons", "room", "timeSlot", "topics"})
    @EqualsAndHashCode.Exclude
    @Relationship(type = "IS_ABOUT", direction = Relationship.INCOMING)
    private List<Talk> talks;
}
