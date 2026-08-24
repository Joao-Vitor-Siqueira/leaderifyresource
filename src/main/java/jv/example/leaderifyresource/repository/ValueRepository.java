package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.Value;
import jv.example.leaderifyresource.record.response.ValueResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ValueRepository extends JpaRepository<Value,Long> {
    @Query("""
        SELECT new com.example.ValueResponse(
            v.id,
            m.id,
            e.id,
            v.value
        )
        FROM Value v
        JOIN v.metric m
        JOIN v.entry e
        WHERE v.id = :id
    """)
    Optional<ValueResponse> findValueResponseById(@Param("id") Long id);

    Optional<Value> findByMetricIdAndEntryId(Long metricId, Long entryId);
}
