package jv.example.leaderifyresource.record.request;

public record UserRelationshipRequest(
        Long requesterId,
        Long receiverId
) {
}
