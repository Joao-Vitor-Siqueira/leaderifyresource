package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.Value;
import jv.example.leaderifyresource.record.response.ValueResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ValueRepository extends JpaRepository<Value,Long> {
    Optional<Value> findByMetricIdAndEntryId(Long metricId, Long entryId);
}
