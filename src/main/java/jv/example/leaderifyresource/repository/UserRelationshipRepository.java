package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.UserRelationship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRelationshipRepository extends JpaRepository<UserRelationship, Long> {
}
