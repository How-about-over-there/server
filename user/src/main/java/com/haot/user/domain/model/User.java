package com.haot.user.domain.model;

import com.haot.user.domain.model.enums.Gender;
import com.haot.user.submodule.auditor.BaseEntity;
import com.haot.user.submodule.role.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_user")
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "user_id")
  private String id;

  @Column(name = "name", length = 255, nullable = false)
  private String name;

  @Column(name = "password", length = 255, nullable = false)
  private String password;

  @Column(name = "email", length = 255, nullable = false, unique = true)
  private String email;

  @Column(name = "phone_number", length = 255, nullable = false)
  private String phoneNumber;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  private Role role;

  @Column(name = "birth_date")
  private LocalDate birthDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "gender")
  private Gender gender;

  @Column(name = "preferred_language", length = 5)
  private String preferredLanguage;

  @Column(name = "currency", length = 3)
  private String currency;

  @Column(name = "profile_image_url", length = 255)
  private String profileImageUrl;

  @Column(name = "address", length = 255)
  private String address;

  public static User create(
      String name,
      String password,
      String email,
      String phoneNumber,
      Role role,
      LocalDate birthDate,
      Gender gender,
      String preferredLanguage,
      String currency,
      String profileImageUrl,
      String address
  ) {
    if (role == null) {
      role = Role.USER;
    }
    return User.builder()
        .name(name)
        .password(password)
        .email(email)
        .phoneNumber(phoneNumber)
        .role(role)
        .birthDate(birthDate)
        .gender(gender)
        .preferredLanguage(preferredLanguage)
        .currency(currency)
        .profileImageUrl(profileImageUrl)
        .address(address)
        .build();
  }

}
