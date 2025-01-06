package com.haot.lodge.domain.repository;


import com.haot.lodge.domain.model.LodgeDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LodgeDateRepository extends JpaRepository<LodgeDate, String>, LodgeDateCustomRepository {
}
