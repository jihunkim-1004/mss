package com.mss.shop.mss.controller;

import com.mss.shop.mss.dto.ApiResponseDto;
import com.mss.shop.mss.dto.CategoryPriceRangeDto;
import com.mss.shop.mss.dto.CategoryPriceResponseDto;
import com.mss.shop.mss.dto.LowestPriceResponseDto;
import com.mss.shop.mss.dto.ProductDto;
import com.mss.shop.mss.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  // 1. 카테고리별 최저가격 조회
  @GetMapping("/lowest-price-by-category")
  public ResponseEntity<CategoryPriceResponseDto> getLowestPriceByCategory() {
    return ResponseEntity.ok(productService.getLowestPriceByCategory());
  }

  // 2. 단일 브랜드 최저가격 조회
  @GetMapping("/lowest-price-by-single-brand")
  public ResponseEntity<LowestPriceResponseDto> getLowestPriceBySingleBrand() {
    return ResponseEntity.ok(productService.getLowestPriceBySingleBrand());
  }

  // 3. 카테고리별 최저/최고가격 조회
  @GetMapping("/price-range/{category}")
  public ResponseEntity<CategoryPriceRangeDto> getPriceRangeByCategory(@PathVariable String category) {
    return ResponseEntity.ok(productService.getPriceRangeByCategory(category));
  }

  // 4. 상품 관리 API
  @PostMapping
  public ResponseEntity<ApiResponseDto> createProduct(@RequestBody ProductDto productDto) {
    return ResponseEntity.ok(productService.createProduct(productDto));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponseDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
    return ResponseEntity.ok(productService.updateProduct(id, productDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponseDto> deleteProduct(@PathVariable Long id) {
    return ResponseEntity.ok(productService.deleteProduct(id));
  }
} 