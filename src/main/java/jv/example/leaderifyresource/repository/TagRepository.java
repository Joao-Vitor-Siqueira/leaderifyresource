package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.Tag;
import jv.example.leaderifyresource.record.response.TagResponse;
import jv.example.leaderifyresource.record.response.ValueResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByNameAndTagCategoryId(String name, Long tagCategoryId);
}
