package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.UserRelationship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRelationshipRepository extends JpaRepository<UserRelationship, Long> {
    Optional<UserRelationship> findByUserHighIdAndUserLowId(Long userHighId, Long userLowId);
}
