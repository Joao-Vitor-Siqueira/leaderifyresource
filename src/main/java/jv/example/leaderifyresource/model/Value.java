package jv.example.leaderifyresource.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        name = "value",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_value_pair",
                        columnNames = {"metric_id", "entry_id"}
                )
        }
)
public class Value {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "metric_id", nullable = false)
    private Metric metric;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entry_id", nullable = false)
    private Entry entry;

    @Column(nullable = false)
    private Double value;
}
