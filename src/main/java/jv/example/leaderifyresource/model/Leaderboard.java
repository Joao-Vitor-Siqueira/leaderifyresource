package jv.example.leaderifyresource.model;

import jakarta.persistence.*;
import jv.example.leaderifyresource.utils.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "leaderboard")
@Getter
@Setter
@NoArgsConstructor
public class Leaderboard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "leaderboard")
    private List<Metric> metrics;

    @OneToMany(mappedBy = "leaderboard")
    private List<Entry> entries;

    @OneToMany(mappedBy = "leaderboard")
    private List<TagCategory> tagCategories;
}
