package com.mss.shop.mss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPriceDto {

  @JsonProperty("카테고리")
  private String category;

  @JsonProperty("브랜드")
  private String brand;

  @JsonProperty("가격")
  private Integer price;
} 