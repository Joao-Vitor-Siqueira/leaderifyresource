package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.View;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ViewRepository extends JpaRepository<View,Long> {
    Optional<View> findByNameAndUserLeaderboardId(String name, Long userLeaderboardId);
}
