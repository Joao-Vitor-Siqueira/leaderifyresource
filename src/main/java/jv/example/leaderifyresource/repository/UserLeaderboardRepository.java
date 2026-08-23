package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.UserLeaderboard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLeaderboardRepository extends JpaRepository<UserLeaderboard, Long> {
}
