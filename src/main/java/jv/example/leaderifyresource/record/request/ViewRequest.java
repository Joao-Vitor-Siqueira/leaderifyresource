package jv.example.leaderifyresource.record.request;

import jakarta.validation.constraints.NotEmpty;
import jv.example.leaderifyresource.model.ViewMetricConfig;
import jv.example.leaderifyresource.model.ViewTagConfig;
import jv.example.leaderifyresource.utils.Visibility;

import java.util.List;

public record ViewRequest(
        String name,
        Long userLeaderboardId,
        @NotEmpty(message = "A view must contain at least one metric configuration")
        List<ViewMetricConfigRequest> viewMetricConfigs,
        List<ViewTagConfigRequest> viewTagConfigs,
        Visibility visibility
) {
}
