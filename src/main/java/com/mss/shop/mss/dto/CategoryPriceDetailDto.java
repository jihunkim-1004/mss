package com.mss.shop.mss.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class CategoryPriceDetailDto {
    @JsonProperty("카테고리")
    private String category;
    
    @JsonProperty("가격")
    private String price;

    public CategoryPriceDetailDto(String category, int price) {
        this.category = category;
        this.price = String.format("%,d", price);
    }
} 