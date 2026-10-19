package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.User;
import jv.example.leaderifyresource.utils.Authority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByIdAndAuthority(Long id, Authority authority);
    @Query("SELECT u.username FROM User u WHERE u.id = :id")
    Optional<String> findUsernameById(@Param("id") Long id);
}
