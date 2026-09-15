package jv.example.leaderifyresource.record.request;

import jv.example.leaderifyresource.record.EntryMetricValue;
import jv.example.leaderifyresource.record.MetricSort;

import java.util.List;
import java.util.Map;

public record CalculationRequest(
        String leaderboardName,
        List<EntryMetricValue> entryMetricValues,
        Map<Integer,MetricSort> metricSorts
) {
}
