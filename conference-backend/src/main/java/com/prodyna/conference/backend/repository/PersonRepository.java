package com.prodyna.conference.backend.repository;

import com.prodyna.conference.backend.model.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Repository
public interface PersonRepository extends Neo4jRepository<Person, Long> {
}
