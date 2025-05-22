package com.example.demo9.dto;

import com.example.demo9.constant.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonDto {

  private Long id;

  @NotBlank(message = "이름은 필수 입력입니다.")
  private String name;

  @NotEmpty(message = "이메일은 필수 입력입니다.")
  @Email(message = "이메일 형식을 확인하세요")
  private String email;

  @NotEmpty(message = "비밀번호는 필수 입력입니다.")
  @Length(min = 4, max = 20, message = "비밀번호는 4~20자리로 입력하세요")
  private String password;

  @Column(length = 20)
  private String address;

  @Enumerated(EnumType.STRING)
  private Role role;

}
