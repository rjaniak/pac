package com.prodyna.conference.backend.repository;

import com.prodyna.conference.backend.model.Topic;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Repository
public interface TopicRepository extends Neo4jRepository<Topic, Long> {
    Long deleteByTopicId(String id);

    boolean existsByTopicId(String id);

    @Query("match (n:Topic) where n.topicId in $ids return n")
    Iterable<Topic> findAllByTopicIds(Iterable<String> ids);

    Optional<Topic> findByTopicId(String id);
}
