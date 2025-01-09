package com.haot.lodge.domain.repository;


import com.haot.lodge.domain.model.Lodge;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LodgeRepository extends JpaRepository<Lodge, String>, LodgeCustomRepository {
    Optional<Lodge> findByHostIdAndName(String hostId, String name);
}
