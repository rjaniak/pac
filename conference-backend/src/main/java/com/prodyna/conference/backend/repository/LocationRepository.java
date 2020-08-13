package com.prodyna.conference.backend.repository;

import com.prodyna.conference.backend.model.Location;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Repository
public interface LocationRepository extends Neo4jRepository<Location, Long> {
    Long deleteByLocationId(String id);

    boolean existsByLocationId(String id);

    Optional<Location> findByLocationId(String id);
}
