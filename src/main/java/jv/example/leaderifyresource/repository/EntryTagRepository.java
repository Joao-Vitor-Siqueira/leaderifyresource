package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.EntryTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EntryTagRepository extends JpaRepository<EntryTag, Long> {
    Optional<EntryTag> findByEntryIdAndTagId(long entryId, long tagId);
}
