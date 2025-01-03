package com.haot.lodge.domain.repository;


import com.haot.lodge.domain.model.Lodge;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public interface LodgeRepository extends JpaRepository<Lodge, String> {
    Optional<Lodge> findByHostIdAndName(String hostId, String name);
}
