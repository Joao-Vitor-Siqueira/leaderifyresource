package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.UserLeaderboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLeaderboardRepository extends JpaRepository<UserLeaderboard, Long> {
    Optional<UserLeaderboard> findByUserIdAndLeaderboardId(Long userId, Long leaderboardId);
}
