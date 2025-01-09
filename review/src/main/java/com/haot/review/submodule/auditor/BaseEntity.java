package com.haot.review.submodule.auditor;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class BaseEntity {
  @CreatedDate
  @Column(updatable = false)
  protected LocalDateTime createdAt;
  @CreatedBy
  @Column(updatable = false)
  protected String createBy;
  @LastModifiedDate
  protected LocalDateTime updatedAt;
  @LastModifiedBy
  protected String updatedBy;
  protected LocalDateTime deleteAt;
  protected String deletedBy;
  protected boolean isDeleted;

  public void deleteEntity(String userId) {
    this.isDeleted = true;
    this.deleteAt = LocalDateTime.now();
    this.deletedBy = userId;
  }
}
