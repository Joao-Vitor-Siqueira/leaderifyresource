package jv.example.leaderifyresource.model;

import jakarta.persistence.*;
import jv.example.leaderifyresource.utils.NumericFilter;
import jv.example.leaderifyresource.utils.Sort;
import jv.example.leaderifyresource.utils.Visibility;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "view_metric_config")
public class ViewMetricConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "view_id", nullable = false)
    private View view;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "metric_id", nullable = false)
    private Metric metric;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sort sort;

    @Enumerated(EnumType.STRING)
    private NumericFilter numericFilter;

    @Column
    private Double target;

    @Column(nullable = false)
    private Integer position;

}
