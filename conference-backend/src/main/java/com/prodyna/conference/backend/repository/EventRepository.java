package com.prodyna.conference.backend.repository;

import com.prodyna.conference.backend.model.Event;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Repository
public interface EventRepository extends Neo4jRepository<Event, Long> {
}
