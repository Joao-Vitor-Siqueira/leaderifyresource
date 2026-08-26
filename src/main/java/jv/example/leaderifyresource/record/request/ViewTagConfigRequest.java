package jv.example.leaderifyresource.record.request;

import jv.example.leaderifyresource.utils.Filter;

public record ViewTagConfigRequest(
        Long tagCategoryId,
        Filter filter
) {
}
