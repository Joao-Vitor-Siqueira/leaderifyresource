package jv.example.leaderifyresource.record.response;

import jv.example.leaderifyresource.utils.InvitationStatus;
import jv.example.leaderifyresource.utils.Role;

public record LeaderboardInvitationResponse(
        Long id,
        InvitationStatus status
) {
}
