package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.Leaderboard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {
}
