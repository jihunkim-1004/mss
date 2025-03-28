package com.mss.shop.mss.exception;

import com.mss.shop.mss.dto.ApiResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ProductException.class)
  public ResponseEntity<ApiResponseDto> handleProductException(ProductException e) {
    return ResponseEntity.ok(new ApiResponseDto(false, e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponseDto> handleException(Exception e) {
    return ResponseEntity.ok(new ApiResponseDto(false, "서버 오류가 발생했습니다: " + e.getMessage()));
  }
} 