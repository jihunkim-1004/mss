package com.mss.shop.mss.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryPriceResponseDto {

  private List<CategoryPriceDto> categoryPrices;
  private int totalPrice;

  public CategoryPriceResponseDto(List<CategoryPriceDto> categoryPrices) {
    this.categoryPrices = categoryPrices;
    this.totalPrice = categoryPrices.stream()
        .mapToInt(CategoryPriceDto::getPrice)
        .sum();
  }
} 