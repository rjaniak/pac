package com.prodyna.conference.backend.repository;

import com.prodyna.conference.backend.model.Person;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Repository
public interface PersonRepository extends Neo4jRepository<Person, Long> {
    Long deleteByPersonId(String id);

    boolean existsByPersonId(String id);

    @Query("match (n:Person) where n.personId in $ids return n")
    Iterable<Person> findAllByPersonIds(Iterable<String> ids);

    Optional<Person> findByPersonId(String id);
}
