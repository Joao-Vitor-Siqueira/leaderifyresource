package jv.example.leaderifyresource.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        name = "tag",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_tag_category_name",
                        columnNames = {"tag_category_id", "name"}
                )
        }
)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_category_id", nullable = false)
    private TagCategory tagCategory;
}
