package com.haot.user.domain.model;

import com.haot.submodule.role.Role;
import com.haot.user.domain.model.enums.Gender;
import com.haot.submodule.auditor.BaseEntity;
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

  public void updateName(String name) {
    if (name != null) {
      this.name = name;
    }
  }

  public void updatePassword(String password) {
    if (password != null) {
      this.password = password;
    }
  }

  public void updateEmail(String email) {
    if (email != null) {
      this.email = email;
    }
  }

  public void updatePhoneNumber(String phoneNumber) {
    if (phoneNumber != null) {
      this.phoneNumber = phoneNumber;
    }
  }

  public void updateRole(Role role) {
    if (role != null) {
      this.role = role;
    }
  }

  public void updateBirthDate(LocalDate birthDate) {
    if (birthDate != null) {
      this.birthDate = birthDate;
    }
  }

  public void updateGender(Gender gender) {
    if (gender != null) {
      this.gender = gender;
    }
  }

  public void updatePreferredLanguage(String preferredLanguage) {
    if (preferredLanguage != null) {
      this.preferredLanguage = preferredLanguage;
    }
  }

  public void updateCurrency(String currency) {
    if (currency != null) {
      this.currency = currency;
    }
  }

  public void updateProfileImageUrl(String profileImageUrl) {
    if (profileImageUrl != null) {
      this.profileImageUrl = profileImageUrl;
    }
  }

  public void updateAddress(String address) {
    if (address != null) {
      this.address = address;
    }
  }

}
