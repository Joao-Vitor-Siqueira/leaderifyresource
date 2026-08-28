package jv.example.leaderifyresource.utils;

import java.util.Objects;

public final class LeaderboardUtils {
    private LeaderboardUtils() {}

    public static boolean canCreateView(Role userRole, Long userLeaderboardId) {
        boolean isLeaderboardMember = userLeaderboardId.equals(SecurityUtils.getCurrentUserId());
        boolean isNotViewer = !userRole.equals(Role.VIEWER);
        return isLeaderboardMember && isNotViewer;
    }

    public static boolean canUpdateOrDeleteView(Role userRole, Long viewOwnerId, Long leaderboardOwnerId) {
        Long authenticatedUserId = SecurityUtils.getCurrentUserId();
        boolean isLeaderboardOwner = Objects.equals(authenticatedUserId, leaderboardOwnerId);
        boolean isViewOwner = viewOwnerId.equals(authenticatedUserId);
        boolean isNotViewer = !userRole.equals(Role.VIEWER);

        return isLeaderboardOwner || (isViewOwner && isNotViewer);
    }

}
