package com.prodyna.conference.backend.repository;

import com.prodyna.conference.backend.model.Event;
import com.prodyna.conference.backend.model.Talk;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Repository
public interface EventRepository extends Neo4jRepository<Event, Long> {
    Long deleteByEventId(String id);

    boolean existsByEventId(String id);

    Optional<Event> findByEventId(String id);
}
