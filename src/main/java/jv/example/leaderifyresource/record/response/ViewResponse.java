package jv.example.leaderifyresource.record.response;

import jv.example.leaderifyresource.model.ViewMetricConfig;
import jv.example.leaderifyresource.model.ViewTagConfig;
import jv.example.leaderifyresource.utils.Visibility;

import java.util.List;

public record ViewResponse(
        Long id,
        String name,
        Long leaderboardId,
        Long userLeaderboardId,
        List<ViewMetricConfig> viewMetricConfigs,
        List<ViewTagConfig> viewTagConfigs,
        Visibility visibility
) {
}
