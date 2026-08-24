package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.ViewMetricConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ViewMetricConfigRepository extends JpaRepository<ViewMetricConfig,Long> {
    Optional<ViewMetricConfig> findByViewIdAndMetricId(Long viewId, Long metricId);
}
