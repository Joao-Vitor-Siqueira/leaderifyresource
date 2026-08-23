package jv.example.leaderifyresource.record.response;

import jv.example.leaderifyresource.model.Entry;
import jv.example.leaderifyresource.model.Metric;

import java.util.List;

public record LeaderboardResponse(
        String name,
        String description,
        List<Metric> metrics,
        List<Entry> entries
) {
}
