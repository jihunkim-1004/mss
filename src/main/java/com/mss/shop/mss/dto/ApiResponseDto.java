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
public class ApiResponseDto {

  @JsonProperty("성공여부")
  private boolean success;

  @JsonProperty("메시지")
  private String message;
} 