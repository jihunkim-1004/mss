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
public class LowestPriceResponseDto {

  @JsonProperty("최저가")
  private BrandPriceResponseDto lowestPrice;
} 