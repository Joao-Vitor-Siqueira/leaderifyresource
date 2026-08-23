package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
