package com.example.demo9.dto;

import com.example.demo9.constant.ItemSellStatus;
import com.example.demo9.entity.Item;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {

  private Long id;

  @NotEmpty(message = "상품명을 입력하세요")
  @Column(length = 30)
  @Size(min = 2, max = 30, message = "상품명은 2~30자 까지 사용하실수 있습니다.")
  private String itemName;

  @NotEmpty(message = "상품 가격을 입력하세요")
  private int price;

  @NotEmpty(message = "재고수량을 입력하세요")
  private int stockNumber;

  @Lob
  @NotEmpty(message = "상품 상세설명을 기록하세요.")
  private String itemDetail;

  @Enumerated(EnumType.STRING)
  private ItemSellStatus itemSellStatus;

  // entity to dto
  public static ItemDto createItemDto(Optional<Item> optionalItem) {
    return ItemDto.builder()
            .id(optionalItem.get().getId())
            .itemName(optionalItem.get().getItemName())
            .price(optionalItem.get().getPrice())
            .stockNumber(optionalItem.get().getStockNumber())
            .itemDetail(optionalItem.get().getItemDetail())
            .itemSellStatus(optionalItem.get().getItemSellStatus())
            .build();
  }
}
