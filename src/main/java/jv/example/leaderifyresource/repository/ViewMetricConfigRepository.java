package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.ViewMetricConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ViewMetricConfigRepository extends JpaRepository<ViewMetricConfig,Long> {
    Optional<ViewMetricConfig> findByViewIdAndMetricId(Long viewId, Long metricId);
    @Modifying
    @Query("""
        DELETE FROM ViewMetricConfig vmc
        WHERE vmc.view.userLeaderboard.leaderboard.id = :leaderboardId
    """)
    void deleteAllByLeaderboardId(@Param("leaderboardId") Long leaderboardId);

    List<ViewMetricConfig> findAllByViewId(Long viewId);
}
