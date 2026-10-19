package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.Entry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findAllByLeaderboardId(Long leaderboardId);
    Page<Entry> findAllByLeaderboardId(Long leaderboardId, Pageable pageable);
    boolean existsByLeaderboardIdAndName(Long leaderboardId, String name);
}
