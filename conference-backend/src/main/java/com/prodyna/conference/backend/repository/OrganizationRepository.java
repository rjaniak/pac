package com.prodyna.conference.backend.repository;

import com.prodyna.conference.backend.model.Organization;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Repository
public interface OrganizationRepository extends Neo4jRepository<Organization, Long> {
    Long deleteByOrganizationId(String id);

    boolean existsByOrganizationId(String id);

    Optional<Organization> findByOrganizationId(String id);
}
