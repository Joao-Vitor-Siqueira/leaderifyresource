package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findAllByLeaderboardId(Long leaderboardId);
    boolean existsByLeaderboardIdAndName(Long leaderboardId, String name);
}
