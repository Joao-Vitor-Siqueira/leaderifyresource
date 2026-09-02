package jv.example.leaderifyresource.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "metric")
public class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leaderboard_id",  nullable = false)
    private Leaderboard leaderboard;

    @OneToMany(
            mappedBy = "metric",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Value> values;

    @OneToMany(
            mappedBy = "metric",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ViewMetricConfig> viewMetricConfigs = new ArrayList<>();
}
