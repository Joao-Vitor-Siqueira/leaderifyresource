package jv.example.leaderifyresource.record.response;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
