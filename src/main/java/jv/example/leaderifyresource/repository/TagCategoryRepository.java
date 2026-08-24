package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.TagCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagCategoryRepository extends JpaRepository<TagCategory, Long> {
}
