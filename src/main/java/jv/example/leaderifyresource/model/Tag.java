package jv.example.leaderifyresource.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(
        name = "tag",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_tag_category_name",
                        columnNames = {"tag_category_id", "name"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_category_id", nullable = false)
    private TagCategory tagCategory;

    @OneToMany(mappedBy = "tag")
    private List<EntryTag> entryTags;
}
