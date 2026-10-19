package jv.example.leaderifyresource.record.request;

import jv.example.leaderifyresource.utils.Sort;

public record MetricRequest(
        String name,
        String description,
        Integer position,
        Sort sort
) {}
