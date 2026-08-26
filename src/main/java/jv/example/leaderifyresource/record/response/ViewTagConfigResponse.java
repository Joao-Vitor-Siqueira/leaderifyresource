package jv.example.leaderifyresource.record.response;

import jv.example.leaderifyresource.utils.Filter;

public record ViewTagConfigResponse(
        Long id,
        Filter filter
) {
}
