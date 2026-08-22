package jv.example.leaderifyresource.record;

import java.time.Instant;

public record ErrorResponse(
        int status,
        String code,
        String message,
        Instant timestamp,
        String path
) {}
