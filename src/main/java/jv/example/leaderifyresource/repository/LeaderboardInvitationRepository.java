package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.LeaderboardInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaderboardInvitationRepository extends JpaRepository<LeaderboardInvitation, Long> {
}
