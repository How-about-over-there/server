package com.haot.lodge.domain.repository;


import com.haot.lodge.domain.model.LodgeRule;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LodgeRuleRepository extends JpaRepository<LodgeRule, String> {
    Optional<LodgeRule> findLodgeRuleByLodgeId(String lodgeId);
}
