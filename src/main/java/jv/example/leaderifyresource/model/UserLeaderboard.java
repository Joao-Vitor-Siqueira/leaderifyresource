package jv.example.leaderifyresource.model;

import jakarta.persistence.*;
import jv.example.leaderifyresource.utils.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        name = "user_leaderboard",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_leaderboard_pair",
                        columnNames = {"user_id", "leaderboard_id"}
                )
        }
)
public class UserLeaderboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leaderboard_id", nullable = false)
    private Leaderboard leaderboard;

    @OneToMany(
            mappedBy = "userLeaderboard",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<View> views = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private Instant joinedAt;

    public void addView(View view) {
        views.add(view);
        view.setUserLeaderboard(this);
    }

    @PrePersist
    protected void onCreate() {
        joinedAt = Instant.now();
    }
}
