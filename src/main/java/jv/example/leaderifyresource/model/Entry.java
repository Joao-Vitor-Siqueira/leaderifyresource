package jv.example.leaderifyresource.model;

import jakarta.persistence.*;
import jv.example.leaderifyresource.utils.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        name = "entry",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_entry_pair",
                        columnNames = {"name", "leaderboard_id"}
                )
        }
)
public class Entry extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leaderboard_id", nullable = false)
    private Leaderboard leaderboard;

    @OneToMany(
            mappedBy = "entry",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<EntryTag> entryTags;

    @OneToMany(
            mappedBy = "entry",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Value> values;
}
