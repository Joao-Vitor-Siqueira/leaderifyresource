package jv.example.leaderifyresource.model;

import jakarta.persistence.*;
import jv.example.leaderifyresource.utils.Visibility;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "view")
@Getter
@Setter
@NoArgsConstructor
public class View {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leaderboard_id")
    private Leaderboard leaderboard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_leaderboard_id")
    private UserLeaderboard userLeaderboard;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Column(nullable = false)
    private String name;
}
