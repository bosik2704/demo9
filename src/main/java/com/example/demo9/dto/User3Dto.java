package com.example.demo9.dto;

import com.example.demo9.entity.User3;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User3Dto {
  private Long id;
  private String name;
  private int age;
  private String gender;
  private String address;

  // Entity to dto
  public static User3Dto createUser(Optional<User3> user3) {
    User3Dto dto = User3Dto.builder()
            .id(user3.get().getId())
            .name(user3.get().getName())
            .age(user3.get().getAge())
            .gender(user3.get().getGender())
            .address(user3.get().getAddress())
            .build();
    return dto;
  }
}
