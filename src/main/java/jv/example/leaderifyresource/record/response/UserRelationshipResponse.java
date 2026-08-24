package jv.example.leaderifyresource.record.response;

import jv.example.leaderifyresource.utils.RelationshipStatus;

public record UserRelationshipResponse(
        Long id,
        Long userHighId,
        Long userLowId,
        RelationshipStatus status
) {
}
