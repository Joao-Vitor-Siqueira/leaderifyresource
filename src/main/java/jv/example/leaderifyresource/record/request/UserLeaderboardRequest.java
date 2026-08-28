package jv.example.leaderifyresource.record.request;

import jv.example.leaderifyresource.utils.Role;

public record UserLeaderboardRequest(
        Long userId,
        Role role
) {
}
