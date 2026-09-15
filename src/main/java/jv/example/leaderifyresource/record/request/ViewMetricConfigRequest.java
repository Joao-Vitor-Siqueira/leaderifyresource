package jv.example.leaderifyresource.record.request;

import jakarta.validation.constraints.NotNull;
import jv.example.leaderifyresource.utils.NumericFilter;
import jv.example.leaderifyresource.utils.Sort;
import jv.example.leaderifyresource.utils.Visibility;

public record ViewMetricConfigRequest(
        Visibility visibility,
        @NotNull(message = "Sort must not be null")
        Sort sort,
        NumericFilter numericFilter,
        Double target,
        @NotNull(message = "Position must not be null")
        Integer position
) {
}
