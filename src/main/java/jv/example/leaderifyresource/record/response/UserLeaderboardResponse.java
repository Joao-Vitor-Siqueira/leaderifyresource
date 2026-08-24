package jv.example.leaderifyresource.record.response;

import jv.example.leaderifyresource.utils.Role;

public record UserLeaderboardResponse(
        Long id,
        Long userId,
        Long LeaderboardId,
        Role role
) {
}
