package com.prodyna.conference.backend.repository;

import com.prodyna.conference.backend.model.Room;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Repository
public interface RoomRepository extends Neo4jRepository<Room, Long> {
    Long deleteByRoomId(String id);

    boolean existsByRoomId(String id);

    Optional<Room> findByRoomId(String id);
}
