package com.example.demo9.entity;

import com.example.demo9.dto.User3Dto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User3 {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user3_id")
  private Long id;

  @Column(length = 20, nullable = false)
  private String name;

  @ColumnDefault("20")
  private int age;

  private String gender;

  private String address;

  // user등록(dto to Entity)
  public static User3 createUser(User3Dto dto) {
    User3 user = User3.builder()
            .name(dto.getName())
            .age(dto.getAge())
            .gender(dto.getGender())
            .address(dto.getAddress())
            .build();
    return user;
  }
}
