package jv.example.leaderifyresource.record;

import java.util.List;

public record EntryMetricValue(
        String name,
        List<MetricValue> scores
) {
}
