package jv.example.leaderifyresource.record.request;

import jakarta.validation.constraints.NotNull;
import jv.example.leaderifyresource.utils.NumericFilter;
import jv.example.leaderifyresource.utils.Sort;
import jv.example.leaderifyresource.utils.Visibility;

public record ViewMetricConfigRequest(
        @NotNull(message = "Metric Id must not be null")
        Long metricId,
        Visibility visibility,
        Sort sort,
        NumericFilter numericFilter,
        Double target,
        Integer position
) {
}
