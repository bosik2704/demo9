package com.example.demo9.entity;

import com.example.demo9.constant.ItemSellStatus;
import com.example.demo9.dto.ItemDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_id")
  private Long id;

//  @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
//  private List<OrderItem> orderItems = new ArrayList<>();

  @Column(length = 30, nullable = false)
  private String itemName;

  @NotEmpty
  private int price;

  @NotEmpty
  private int stockNumber;

  @Lob
  @NotEmpty
  private String itemDetail;

  @Enumerated(EnumType.STRING)
  private ItemSellStatus itemSellStatus;

  // dto to entity
  public static Item createItem(ItemDto dto) {
    return Item.builder()
            .id(dto.getId())
            .itemName(dto.getItemName())
            .price(dto.getPrice())
            .itemDetail(dto.getItemDetail())
            .stockNumber(dto.getStockNumber())
            //.itemSellStatus(ItemSellStatus.SELL)
            .itemSellStatus(dto.getItemSellStatus())
            .build();
  }
}
