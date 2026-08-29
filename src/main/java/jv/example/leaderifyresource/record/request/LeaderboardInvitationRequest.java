package jv.example.leaderifyresource.record.request;

import jv.example.leaderifyresource.utils.Role;

public record LeaderboardInvitationRequest(
        Long userId,
        Role role
) {
}
