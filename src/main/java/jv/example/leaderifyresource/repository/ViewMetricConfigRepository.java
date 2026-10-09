package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.ViewMetricConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    long countByViewId(Long viewId);

    List<ViewMetricConfig> findAllByViewId(Long viewId);
    Page<ViewMetricConfig> findAllByViewId(Long viewId, Pageable pageable);

    Optional<ViewMetricConfig> findByViewIdAndPosition(Long viewId, Integer position);

    @Modifying
    @Query("""
        UPDATE ViewMetricConfig vm
        SET vm.position = vm.position + :offset
        WHERE vm.view.id = :viewId
          AND vm.position > :deletedPosition
    """)
    int movePositionsTemporarily(
            Long viewId,
            Integer deletedPosition,
            Integer offset
    );

    @Modifying
    @Query("""
        UPDATE ViewMetricConfig vm
        SET vm.position = vm.position - :offset - 1
        WHERE vm.viewId = :viewId
          AND vm.position > :offset
    """)
    int closePositionGap(
            Long viewId,
            Integer offset
    );
}
