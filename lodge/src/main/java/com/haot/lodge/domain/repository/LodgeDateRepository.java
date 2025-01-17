package com.haot.lodge.domain.repository;


import com.haot.lodge.domain.model.LodgeDate;
import com.haot.lodge.infrastructure.repository.LodgeDateCustomRepository;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LodgeDateRepository extends JpaRepository<LodgeDate, String>, LodgeDateCustomRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ld FROM LodgeDate ld WHERE ld.id = :id")
    Optional<LodgeDate> findByIdWithLock(String id);
}
