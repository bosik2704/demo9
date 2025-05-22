package com.example.demo9.entity;

import com.example.demo9.constant.Role;
import com.example.demo9.dto.PersonDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "person_id")
  private Long id;

  @Column(length = 20)
  private String name;

  @Column(length = 50)
  private String email;

  @Column(length = 100)
  private String password;

  @Column(length = 20)
  private String address;

  @Enumerated(EnumType.STRING)
  private Role role;

  // dto to entity
  public static Person createPerson(PersonDto dto, PasswordEncoder passwordEncoder) {

    return null;
  }

}
