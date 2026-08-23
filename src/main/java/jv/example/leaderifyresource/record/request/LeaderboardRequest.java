package jv.example.leaderifyresource.record.request;

import jv.example.leaderifyresource.model.Entry;
import jv.example.leaderifyresource.model.Metric;
import jv.example.leaderifyresource.model.TagCategory;

import java.util.List;

public record LeaderboardRequest(
        String name,
        String description,
        List<Metric> metrics,
        List<Entry> entries,
        List<TagCategory> tagCategories
) {
}
