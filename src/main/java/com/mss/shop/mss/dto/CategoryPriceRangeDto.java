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
public class CategoryPriceRangeDto {

  @JsonProperty("카테고리")
  private String category;

  @JsonProperty("최저가")
  private List<CategoryPriceDetailDto> lowestPrices;

  @JsonProperty("최고가")
  private List<CategoryPriceDetailDto> highestPrices;
} 