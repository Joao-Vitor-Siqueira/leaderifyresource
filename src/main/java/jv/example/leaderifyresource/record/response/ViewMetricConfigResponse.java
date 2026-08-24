package jv.example.leaderifyresource.record.response;

import jv.example.leaderifyresource.utils.NumericFilter;
import jv.example.leaderifyresource.utils.Sort;
import jv.example.leaderifyresource.utils.Visibility;

public record ViewMetricConfigResponse(
        Long id,
        Long viewId,
        Long metricId,
        Visibility visibility,
        Sort sort,
        NumericFilter numericFilter,
        Double target,
        Integer position
) {
}
