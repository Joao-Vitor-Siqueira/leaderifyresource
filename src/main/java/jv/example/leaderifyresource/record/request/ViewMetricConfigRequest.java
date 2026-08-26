package jv.example.leaderifyresource.record.request;

import jv.example.leaderifyresource.utils.NumericFilter;
import jv.example.leaderifyresource.utils.Sort;
import jv.example.leaderifyresource.utils.Visibility;

public record ViewMetricConfigRequest(
        Long metricId,
        Visibility visibility,
        Sort sort,
        NumericFilter numericFilter,
        Double target,
        Integer position
) {
}
