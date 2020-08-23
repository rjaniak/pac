package com.prodyna.conference.backend.repository;

import com.prodyna.conference.backend.model.Talk;
import com.prodyna.conference.backend.model.Topic;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Repository
public interface TalkRepository extends Neo4jRepository<Talk, Long> {
    Long deleteByTalkId(String id);

    boolean existsByTalkId(String id);

    Optional<Talk> findByTalkId(String id);

    @Query("MATCH (:Event{eventId: $eventId})<-[:PART_OF]-(talk:Talk) RETURN talk")
    Iterable<Talk> findAllByEventId(String eventId);
}
