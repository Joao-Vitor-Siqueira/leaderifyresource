package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, Long> {
}
