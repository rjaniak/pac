package com.prodyna.conference.backend.repository;

import com.prodyna.conference.backend.model.Talk;
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
}
