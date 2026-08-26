package jv.example.leaderifyresource.model;

import jakarta.persistence.*;
import jv.example.leaderifyresource.utils.Visibility;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        name = "view",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_view_leaderboard_name",
                        columnNames = {"leaderboard_id", "name"}
                )
        }
)
public class View {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leaderboard_id", nullable = false)
    private Leaderboard leaderboard;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_leaderboard_id", nullable = false)
    private UserLeaderboard userLeaderboard;

    @OneToMany(mappedBy = "view")
    private List<ViewMetricConfig> viewMetricConfigs;

    @OneToMany(mappedBy = "view")
    private List<ViewTagConfig> viewTagConfigs;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

}
