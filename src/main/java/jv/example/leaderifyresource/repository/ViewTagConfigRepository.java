package jv.example.leaderifyresource.repository;
import jv.example.leaderifyresource.model.ViewTagConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ViewTagConfigRepository extends JpaRepository<ViewTagConfig,Long> {
    Optional<ViewTagConfig> findByViewIdAndTagCategoryId(Long viewId, Long tagCategoryId);
}
