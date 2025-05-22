package com.example.demo9.entity;

import com.example.demo9.constant.ItemSellStatus;
import com.example.demo9.dto.ItemDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cart_item_id")
  private Long id;

  @Column(length = 30, nullable = false)
  private String itemName;

  @NotEmpty
  private int count;

  @NotEmpty
  private int price;

}
