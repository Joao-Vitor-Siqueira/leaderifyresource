package jv.example.leaderifyresource.record.request;

import jakarta.validation.constraints.NotNull;
import jv.example.leaderifyresource.utils.Filter;

public record ViewTagConfigRequest(
        @NotNull(message = "Tag Category Id must not be null")
        Long tagCategoryId,
        Filter filter,
        String target
) {
}
