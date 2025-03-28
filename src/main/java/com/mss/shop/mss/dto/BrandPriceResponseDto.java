package com.mss.shop.mss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandPriceResponseDto {

  @JsonProperty("브랜드")
  private String brand;

  @JsonProperty("카테고리")
  private List<CategoryPriceDetailDto> categories;

  @JsonProperty("총액")
  private String totalPrice;

  public BrandPriceResponseDto(String brand, List<CategoryPriceDetailDto> categories, int totalPrice) {
    this.brand = brand;
    this.categories = categories;
    this.totalPrice = String.format("%,d", totalPrice);
  }
} 