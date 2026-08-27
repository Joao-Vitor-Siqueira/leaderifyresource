package jv.example.leaderifyresource.record.request;

import jakarta.validation.constraints.NotEmpty;
import jv.example.leaderifyresource.model.ViewMetricConfig;
import jv.example.leaderifyresource.model.ViewTagConfig;
import jv.example.leaderifyresource.utils.Visibility;

import java.util.List;

public record ViewRequest(
        String name,
        Long userLeaderboardId,
        Visibility visibility
) {
}
