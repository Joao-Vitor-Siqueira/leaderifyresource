package jv.example.leaderifyresource.model;

import jakarta.persistence.*;
import jv.example.leaderifyresource.utils.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        name = "leaderboard",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_leaderboard_owner_name",
                        columnNames = {"owner_id", "name"}
                )
        }
)
public class Leaderboard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(
            mappedBy = "leaderboard",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Metric> metrics = new ArrayList<>();

    @OneToMany(
            mappedBy = "leaderboard",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Entry> entries = new ArrayList<>();

    @OneToMany(
            mappedBy = "leaderboard",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TagCategory> tagCategories = new ArrayList<>();

    @OneToMany(
            mappedBy = "leaderboard",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<View> views = new ArrayList<>();

    public void addMetric(Metric metric) {
        metrics.add(metric);
        metric.setLeaderboard(this);
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
        entry.setLeaderboard(this);
    }

    public void addTagCategory(TagCategory tagCategory) {
        tagCategories.add(tagCategory);
        tagCategory.setLeaderboard(this);
    }

    public void addView(View view) {
        views.add(view);
        view.setLeaderboard(this);
    }
}
