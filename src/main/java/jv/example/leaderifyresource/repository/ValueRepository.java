package jv.example.leaderifyresource.repository;

import jv.example.leaderifyresource.model.Value;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValueRepository extends JpaRepository<Value,Long> {
}
