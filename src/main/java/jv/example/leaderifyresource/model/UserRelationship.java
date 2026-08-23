package jv.example.leaderifyresource.model;

import jakarta.persistence.*;
import jv.example.leaderifyresource.utils.BaseEntity;
import jv.example.leaderifyresource.utils.RelationshipStatus;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(
        name = "user_relationship",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_relationship_pair",
                        columnNames = {"user_low_id", "user_high_id"}
                )
        }
)

@NoArgsConstructor
public class UserRelationship extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_low_id", nullable = false)
    private User userLow;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_high_id", nullable = false)
    private User userHigh;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RelationshipStatus status;

    public static UserRelationship create(
            User requester,
            User receiver
    ) {
        if (requester.getId().equals(receiver.getId())) {
            throw new IllegalArgumentException(
                    "A user cannot have a friendship with themselves"
            );
        }

        UserRelationship friendship = new UserRelationship();

        if (requester.getId() < receiver.getId()) {
            friendship.userLow = requester;
            friendship.userHigh = receiver;
        } else {
            friendship.userLow = receiver;
            friendship.userHigh = requester;
        }

        friendship.status = Objects.equals(requester.getId(), friendship.userLow.getId()) ?
                RelationshipStatus.PENDING_LOW_HIGH : RelationshipStatus.PENDING_HIGH_LOW;

        return friendship;
    }
}
