package com.haot.lodge.domain.repository;

import com.haot.lodge.domain.model.LodgeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus
public interface LodgeImageRepository extends JpaRepository<LodgeImage, String> {
}
