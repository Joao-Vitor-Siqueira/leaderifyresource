package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.Metric;
import jv.example.leaderifyresource.record.response.MetricResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricRepository extends JpaRepository<Metric, Long> {
    Page<Metric> findAllByLeaderboardId(Long leaderboardId, Pageable pageable);
    Integer countByLeaderboardId(Long leaderboardId);
}
