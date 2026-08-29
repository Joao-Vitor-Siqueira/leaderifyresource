package jv.example.leaderifyresource.record.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jv.example.leaderifyresource.utils.Sort;

import java.util.List;

public record LeaderboardCreationRequest(
        @NotNull(message = "A leaderboard must have a name")
        String name,
        String description,
        @NotEmpty(message = "A leaderboard must contain at least one metric")
        List<MetricRequest> metrics,
        @NotEmpty(message = "A leaderboard must contain metric configurations")
        List<Sort> metricSorts
) {
}
