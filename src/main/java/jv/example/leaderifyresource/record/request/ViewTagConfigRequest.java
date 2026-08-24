package jv.example.leaderifyresource.record.request;

import jv.example.leaderifyresource.utils.Filter;

public record ViewTagConfigRequest(
        Long viewId,
        Long tagCategoryId,
        Filter filter
) {
}
