package jv.example.leaderifyresource.record.response;

import jv.example.leaderifyresource.record.EntryMetricValue;

import java.util.List;

public record CalculationResponse(
        List<EntryMetricValue> entryMetricValues
) {
}
