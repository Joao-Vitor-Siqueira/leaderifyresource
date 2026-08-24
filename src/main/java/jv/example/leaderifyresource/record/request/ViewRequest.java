package jv.example.leaderifyresource.record.request;

import jv.example.leaderifyresource.model.ViewMetricConfig;
import jv.example.leaderifyresource.model.ViewTagConfig;
import jv.example.leaderifyresource.utils.Visibility;

import java.util.List;

public record ViewRequest(
        String name,
        Long leaderboardId,
        Long userLeaderboardId,
        List<ViewMetricConfig> viewMetricConfigs,
        List<ViewTagConfig> viewTagConfigs,
        Visibility visibility
) {
}
