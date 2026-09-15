package jv.example.leaderifyresource.record.request;

import jakarta.validation.constraints.NotNull;
import jv.example.leaderifyresource.utils.Filter;

public record ViewTagConfigRequest(
        @NotNull(message = "Filter must not be null")
        Filter filter,
        @NotNull(message = "Target must not be null")
        String target
) {
}
