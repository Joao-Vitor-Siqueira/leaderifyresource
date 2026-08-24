package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.Metric;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricRepository extends JpaRepository<Metric, Long> {
}
