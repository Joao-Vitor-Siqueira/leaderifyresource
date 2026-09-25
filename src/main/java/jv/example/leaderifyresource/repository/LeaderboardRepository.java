package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.Leaderboard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {
    boolean existsByOwner_IdAndName(Long ownerId, String name);
    Page<Leaderboard> findByNameContainingIgnoreCase(Pageable pageable, String name);
}
