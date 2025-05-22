package com.example.demo9.entity;

import com.example.demo9.dto.GuestDto;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "guest2")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Guest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "guest_id")
  private Long id;

  @Column(length = 20, nullable = false)
  private String name;

  @Lob
  @NotNull
  private String content;

  @Column(length = 20, nullable = true)
  private String email;

  @Column(length = 50, nullable = true)
  private String homePage;

  @CreatedDate
//  @Temporal(TemporalType.TIMESTAMP)
  private LocalDateTime visitDate;

  @Column(length = 20, nullable = false)
  private String hostIp;

  // dto to Entity
  public static Guest createGuest(GuestDto dto) {
    return Guest.builder()
            .id(dto.getId())
            .name(dto.getName())
            .content(dto.getContent())
            .email(dto.getEmail())
            .homePage(dto.getHomePage())
            .visitDate(dto.getVisitDate())
            .hostIp(dto.getHostIp())
            .build();
  }
}
