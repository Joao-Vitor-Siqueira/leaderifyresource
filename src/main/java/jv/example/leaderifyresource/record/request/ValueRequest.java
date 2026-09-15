package jv.example.leaderifyresource.record.request;

import jv.example.leaderifyresource.model.Entry;
import jv.example.leaderifyresource.model.Metric;

public record ValueRequest(
        Long metricId,
        Long entryId,
        double value
) {
}
