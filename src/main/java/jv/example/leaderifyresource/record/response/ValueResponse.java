package jv.example.leaderifyresource.record.response;

public record ValueResponse(
        Long id,
        Long metricId,
        Long entryId,
        double value
) {
}
